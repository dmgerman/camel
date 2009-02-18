begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * ResolverUtil is used to locate classes that are available in the/a class path  * and meet arbitrary conditions. The two most common conditions are that a  * class implements/extends another class, or that is it annotated with a  * specific annotation. However, through the use of the {@link Test} class it is  * possible to search using arbitrary conditions.  *<p/>  * A ClassLoader is used to locate all locations (directories and jar files) in  * the class path that contain classes within certain packages, and then to load  * those classes and check them. By default the ClassLoader returned by  * {@code Thread.currentThread().getContextClassLoader()} is used, but this can  * be overridden by calling {@link #setClassLoaders(Set)} prior to  * invoking any of the {@code find()} methods.  *<p/>  * General searches are initiated by calling the  * {@link #find(org.apache.camel.util.ResolverUtil.Test, String)}} method and supplying a package  * name and a Test instance. This will cause the named package<b>and all  * sub-packages</b> to be scanned for classes that meet the test. There are  * also utility methods for the common use cases of scanning multiple packages  * for extensions of particular classes, or classes annotated with a specific  * annotation.  *<p/>  * The standard usage pattern for the ResolverUtil class is as follows:  *<pre>  * resolverUtil&lt;ActionBean&gt; resolver = new ResolverUtil&lt;ActionBean&gt;();  * resolver.findImplementation(ActionBean.class, pkg1, pkg2);  * resolver.find(new CustomTest(), pkg1);  * resolver.find(new CustomTest(), pkg2);  * collection&lt;ActionBean&gt; beans = resolver.getClasses();  *</pre>  *  * @author Tim Fennell  */
end_comment

begin_class
DECL|class|ResolverUtil
specifier|public
class|class
name|ResolverUtil
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ResolverUtil
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * A simple interface that specifies how to test classes to determine if      * they are to be included in the results produced by the ResolverUtil.      */
DECL|interface|Test
specifier|public
specifier|static
interface|interface
name|Test
block|{
comment|/**          * Will be called repeatedly with candidate classes. Must return True if          * a class is to be included in the results, false otherwise.          */
DECL|method|matches (Class type)
name|boolean
name|matches
parameter_list|(
name|Class
name|type
parameter_list|)
function_decl|;
block|}
comment|/**      * A Test that checks to see if each class is assignable to the provided      * class. Note that this test will match the parent type itself if it is      * presented for matching.      */
DECL|class|IsA
specifier|public
specifier|static
class|class
name|IsA
implements|implements
name|Test
block|{
DECL|field|parent
specifier|private
name|Class
name|parent
decl_stmt|;
comment|/**          * Constructs an IsA test using the supplied Class as the parent          * class/interface.          */
DECL|method|IsA (Class parentType)
specifier|public
name|IsA
parameter_list|(
name|Class
name|parentType
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parentType
expr_stmt|;
block|}
comment|/**          * Returns true if type is assignable to the parent type supplied in the          * constructor.          */
DECL|method|matches (Class type)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
return|return
name|type
operator|!=
literal|null
operator|&&
name|parent
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"is assignable to "
operator|+
name|parent
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
block|}
comment|/**      * A Test that checks to see if each class is annotated with a specific      * annotation. If it is, then the test returns true, otherwise false.      */
DECL|class|AnnotatedWith
specifier|public
specifier|static
class|class
name|AnnotatedWith
implements|implements
name|Test
block|{
DECL|field|annotation
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
decl_stmt|;
DECL|field|checkMetaAnnotations
specifier|private
name|boolean
name|checkMetaAnnotations
decl_stmt|;
comment|/**          * Constructs an AnnotatedWith test for the specified annotation type.          */
DECL|method|AnnotatedWith (Class<? extends Annotation> annotation)
specifier|public
name|AnnotatedWith
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
parameter_list|)
block|{
name|this
argument_list|(
name|annotation
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**          * Constructs an AnnotatedWith test for the specified annotation type.          */
DECL|method|AnnotatedWith (Class<? extends Annotation> annotation, boolean checkMetaAnnotations)
specifier|public
name|AnnotatedWith
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
parameter_list|,
name|boolean
name|checkMetaAnnotations
parameter_list|)
block|{
name|this
operator|.
name|annotation
operator|=
name|annotation
expr_stmt|;
name|this
operator|.
name|checkMetaAnnotations
operator|=
name|checkMetaAnnotations
expr_stmt|;
block|}
comment|/**          * Returns true if the type is annotated with the class provided to the          * constructor.          */
DECL|method|matches (Class type)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
return|return
name|type
operator|!=
literal|null
operator|&&
name|ObjectHelper
operator|.
name|hasAnnotation
argument_list|(
name|type
argument_list|,
name|annotation
argument_list|,
name|checkMetaAnnotations
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"annotated with @"
operator|+
name|annotation
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
block|}
comment|/**      * A Test that checks to see if each class is annotated with a specific      * annotation. If it is, then the test returns true, otherwise false.      */
DECL|class|AnnotatedWithAny
specifier|public
specifier|static
class|class
name|AnnotatedWithAny
implements|implements
name|Test
block|{
DECL|field|annotations
specifier|private
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
decl_stmt|;
DECL|field|checkMetaAnnotations
specifier|private
name|boolean
name|checkMetaAnnotations
decl_stmt|;
comment|/**          * Constructs an AnnotatedWithAny test for any of the specified annotation types.          */
DECL|method|AnnotatedWithAny (Set<Class<? extends Annotation>> annotations)
specifier|public
name|AnnotatedWithAny
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
parameter_list|)
block|{
name|this
argument_list|(
name|annotations
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**          * Constructs an AnnotatedWithAny test for any of the specified annotation types.          */
DECL|method|AnnotatedWithAny (Set<Class<? extends Annotation>> annotations, boolean checkMetaAnnotations)
specifier|public
name|AnnotatedWithAny
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
parameter_list|,
name|boolean
name|checkMetaAnnotations
parameter_list|)
block|{
name|this
operator|.
name|annotations
operator|=
name|annotations
expr_stmt|;
name|this
operator|.
name|checkMetaAnnotations
operator|=
name|checkMetaAnnotations
expr_stmt|;
block|}
comment|/**          * Returns true if the type is annotated with the class provided to the          * constructor.          */
DECL|method|matches (Class type)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Class
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|hasAnnotation
argument_list|(
name|type
argument_list|,
name|annotation
argument_list|,
name|checkMetaAnnotations
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"annotated with any @["
operator|+
name|annotations
operator|+
literal|"]"
return|;
block|}
block|}
comment|/**      * The set of matches being accumulated.      */
DECL|field|matches
specifier|private
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|matches
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * The ClassLoader to use when looking for classes. If null then the      * ClassLoader returned by Thread.currentThread().getContextClassLoader()      * will be used.      */
DECL|field|classLoaders
specifier|private
name|Set
argument_list|<
name|ClassLoader
argument_list|>
name|classLoaders
decl_stmt|;
comment|/**      * Provides access to the classes discovered so far. If no calls have been      * made to any of the {@code find()} methods, this set will be empty.      *      * @return the set of classes that have been discovered.      */
DECL|method|getClasses ()
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|>
name|getClasses
parameter_list|()
block|{
return|return
name|matches
return|;
block|}
comment|/**      * Returns the classloaders that will be used for scanning for classes. If no      * explicit ClassLoader has been set by the calling, the context class      * loader will and the one that has loaded this class ResolverUtil be used.      *      * @return the ClassLoader instances that will be used to scan for classes      */
DECL|method|getClassLoaders ()
specifier|public
name|Set
argument_list|<
name|ClassLoader
argument_list|>
name|getClassLoaders
parameter_list|()
block|{
if|if
condition|(
name|classLoaders
operator|==
literal|null
condition|)
block|{
name|classLoaders
operator|=
operator|new
name|HashSet
argument_list|<
name|ClassLoader
argument_list|>
argument_list|()
expr_stmt|;
name|ClassLoader
name|ccl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|ccl
operator|!=
literal|null
condition|)
block|{
name|classLoaders
operator|.
name|add
argument_list|(
name|ccl
argument_list|)
expr_stmt|;
block|}
name|classLoaders
operator|.
name|add
argument_list|(
name|ResolverUtil
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|classLoaders
return|;
block|}
comment|/**      * Sets the ClassLoader instances that should be used when scanning for      * classes. If none is set then the context classloader will be used.      *      * @param classLoaders a ClassLoader to use when scanning for classes      */
DECL|method|setClassLoaders (Set<ClassLoader> classLoaders)
specifier|public
name|void
name|setClassLoaders
parameter_list|(
name|Set
argument_list|<
name|ClassLoader
argument_list|>
name|classLoaders
parameter_list|)
block|{
name|this
operator|.
name|classLoaders
operator|=
name|classLoaders
expr_stmt|;
block|}
comment|/**      * Attempts to discover classes that are assignable to the type provided. In      * the case that an interface is provided this method will collect      * implementations. In the case of a non-interface class, subclasses will be      * collected. Accumulated classes can be accessed by calling      * {@link #getClasses()}.      *      * @param parent       the class of interface to find subclasses or      *                     implementations of      * @param packageNames one or more package names to scan (including      *                     subpackages) for classes      */
DECL|method|findImplementations (Class parent, String... packageNames)
specifier|public
name|void
name|findImplementations
parameter_list|(
name|Class
name|parent
parameter_list|,
name|String
modifier|...
name|packageNames
parameter_list|)
block|{
if|if
condition|(
name|packageNames
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Searching for implementations of "
operator|+
name|parent
operator|.
name|getName
argument_list|()
operator|+
literal|" in packages: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|packageNames
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Test
name|test
init|=
operator|new
name|IsA
argument_list|(
name|parent
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|pkg
range|:
name|packageNames
control|)
block|{
name|find
argument_list|(
name|test
argument_list|,
name|pkg
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found: "
operator|+
name|getClasses
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Attempts to discover classes that are annotated with to the annotation.      * Accumulated classes can be accessed by calling {@link #getClasses()}.      *      * @param annotation   the annotation that should be present on matching      *                     classes      * @param packageNames one or more package names to scan (including      *                     subpackages) for classes      */
DECL|method|findAnnotated (Class<? extends Annotation> annotation, String... packageNames)
specifier|public
name|void
name|findAnnotated
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotation
parameter_list|,
name|String
modifier|...
name|packageNames
parameter_list|)
block|{
if|if
condition|(
name|packageNames
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Searching for annotations of "
operator|+
name|annotation
operator|.
name|getName
argument_list|()
operator|+
literal|" in packages: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|packageNames
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Test
name|test
init|=
operator|new
name|AnnotatedWith
argument_list|(
name|annotation
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|pkg
range|:
name|packageNames
control|)
block|{
name|find
argument_list|(
name|test
argument_list|,
name|pkg
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found: "
operator|+
name|getClasses
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Attempts to discover classes that are annotated with any of the annotation.      * Accumulated classes can be accessed by calling {@link #getClasses()}.      *      * @param annotations  the annotations that should be present on matching      *                     classes      * @param packageNames one or more package names to scan (including      *                     subpackages) for classes      */
DECL|method|findAnnotated (Set<Class<? extends Annotation>> annotations, String... packageNames)
specifier|public
name|void
name|findAnnotated
parameter_list|(
name|Set
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
parameter_list|,
name|String
modifier|...
name|packageNames
parameter_list|)
block|{
if|if
condition|(
name|packageNames
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Searching for annotations of "
operator|+
name|annotations
operator|+
literal|" in packages: "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|packageNames
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Test
name|test
init|=
operator|new
name|AnnotatedWithAny
argument_list|(
name|annotations
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|pkg
range|:
name|packageNames
control|)
block|{
name|find
argument_list|(
name|test
argument_list|,
name|pkg
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found: "
operator|+
name|getClasses
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Scans for classes starting at the package provided and descending into      * subpackages. Each class is offered up to the Test as it is discovered,      * and if the Test returns true the class is retained. Accumulated classes      * can be fetched by calling {@link #getClasses()}.      *      * @param test        an instance of {@link Test} that will be used to filter      *                    classes      * @param packageName the name of the package from which to start scanning      *                    for classes, e.g. {@code net.sourceforge.stripes}      */
DECL|method|find (Test test, String packageName)
specifier|public
name|void
name|find
parameter_list|(
name|Test
name|test
parameter_list|,
name|String
name|packageName
parameter_list|)
block|{
name|packageName
operator|=
name|packageName
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|ClassLoader
argument_list|>
name|set
init|=
name|getClassLoaders
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using only regular classloaders"
argument_list|)
expr_stmt|;
for|for
control|(
name|ClassLoader
name|classLoader
range|:
name|set
control|)
block|{
name|find
argument_list|(
name|test
argument_list|,
name|packageName
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Tries to find the reosurce in the package using the class loader.      *<p/>      * Will handle both plain URL based classloaders and OSGi bundle loaders.      *      * @param test what to find      * @param packageName the package to search in      * @param loader the class loader           */
DECL|method|find (Test test, String packageName, ClassLoader loader)
specifier|protected
name|void
name|find
parameter_list|(
name|Test
name|test
parameter_list|,
name|String
name|packageName
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Searching for: "
operator|+
name|test
operator|+
literal|" in package: "
operator|+
name|packageName
operator|+
literal|" using classloader: "
operator|+
name|loader
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|urls
decl_stmt|;
try|try
block|{
name|urls
operator|=
name|getResources
argument_list|(
name|loader
argument_list|,
name|packageName
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"No URLs returned by classloader"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not read package: "
operator|+
name|packageName
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
return|return;
block|}
while|while
condition|(
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|URL
name|url
init|=
literal|null
decl_stmt|;
try|try
block|{
name|url
operator|=
name|urls
operator|.
name|nextElement
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"URL from classloader: "
operator|+
name|url
argument_list|)
expr_stmt|;
block|}
name|String
name|urlPath
init|=
name|url
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|urlPath
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|urlPath
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Decoded urlPath: "
operator|+
name|urlPath
argument_list|)
expr_stmt|;
block|}
comment|// If it's a file in a directory, trim the stupid file: spec
if|if
condition|(
name|urlPath
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|)
block|{
name|urlPath
operator|=
name|urlPath
operator|.
name|substring
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
comment|// osgi bundles should be skipped
if|if
condition|(
name|url
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"bundle:"
argument_list|)
operator|||
name|urlPath
operator|.
name|startsWith
argument_list|(
literal|"bundle:"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"It's a virtual osgi bundle, skipping"
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|// Else it's in a JAR, grab the path to the jar
if|if
condition|(
name|urlPath
operator|.
name|indexOf
argument_list|(
literal|'!'
argument_list|)
operator|>
literal|0
condition|)
block|{
name|urlPath
operator|=
name|urlPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|urlPath
operator|.
name|indexOf
argument_list|(
literal|'!'
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Scanning for classes in ["
operator|+
name|urlPath
operator|+
literal|"] matching criteria: "
operator|+
name|test
argument_list|)
expr_stmt|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|urlPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loading from directory: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
name|loadImplementationsInDirectory
argument_list|(
name|test
argument_list|,
name|packageName
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|InputStream
name|stream
decl_stmt|;
if|if
condition|(
name|urlPath
operator|.
name|startsWith
argument_list|(
literal|"http:"
argument_list|)
condition|)
block|{
comment|// load resources using http such as java webstart
name|LOG
operator|.
name|debug
argument_list|(
literal|"The current jar is accessed via http"
argument_list|)
expr_stmt|;
name|URL
name|urlStream
init|=
operator|new
name|URL
argument_list|(
name|urlPath
argument_list|)
decl_stmt|;
name|URLConnection
name|con
init|=
name|urlStream
operator|.
name|openConnection
argument_list|()
decl_stmt|;
comment|// disable cache mainly to avoid jar file locking on Windows
name|con
operator|.
name|setUseCaches
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|stream
operator|=
name|con
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|stream
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loading from jar: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
name|loadImplementationsInJar
argument_list|(
name|test
argument_list|,
name|packageName
argument_list|,
name|stream
argument_list|,
name|urlPath
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not read entries in url: "
operator|+
name|url
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Strategy to get the resources by the given classloader.      *<p/>      * Notice that in WebSphere platforms there is a {@link org.apache.camel.util.WebSphereResolverUtil}      * to take care of WebSphere's odditiy of resource loading.      *      * @param loader  the classloader      * @param packageName   the packagename for the package to load      * @return  URL's for the given package      * @throws IOException is thrown by the classloader      */
DECL|method|getResources (ClassLoader loader, String packageName)
specifier|protected
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|getResources
parameter_list|(
name|ClassLoader
name|loader
parameter_list|,
name|String
name|packageName
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Getting resource URL for package: "
operator|+
name|packageName
operator|+
literal|" with classloader: "
operator|+
name|loader
argument_list|)
expr_stmt|;
block|}
return|return
name|loader
operator|.
name|getResources
argument_list|(
name|packageName
argument_list|)
return|;
block|}
comment|/**      * Finds matches in a physical directory on a filesystem. Examines all files      * within a directory - if the File object is not a directory, and ends with      *<i>.class</i> the file is loaded and tested to see if it is acceptable      * according to the Test. Operates recursively to find classes within a      * folder structure matching the package structure.      *      * @param test     a Test used to filter the classes that are discovered      * @param parent   the package name up to this directory in the package      *                 hierarchy. E.g. if /classes is in the classpath and we wish to      *                 examine files in /classes/org/apache then the values of      *<i>parent</i> would be<i>org/apache</i>      * @param location a File object representing a directory      */
DECL|method|loadImplementationsInDirectory (Test test, String parent, File location)
specifier|private
name|void
name|loadImplementationsInDirectory
parameter_list|(
name|Test
name|test
parameter_list|,
name|String
name|parent
parameter_list|,
name|File
name|location
parameter_list|)
block|{
name|File
index|[]
name|files
init|=
name|location
operator|.
name|listFiles
argument_list|()
decl_stmt|;
name|StringBuilder
name|builder
init|=
literal|null
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
name|builder
operator|=
operator|new
name|StringBuilder
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|trim
argument_list|()
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|parent
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|String
name|packageOrClass
init|=
name|parent
operator|==
literal|null
condition|?
name|name
else|:
name|builder
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|loadImplementationsInDirectory
argument_list|(
name|test
argument_list|,
name|packageOrClass
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".class"
argument_list|)
condition|)
block|{
name|addIfMatching
argument_list|(
name|test
argument_list|,
name|packageOrClass
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Finds matching classes within a jar files that contains a folder      * structure matching the package structure. If the File is not a JarFile or      * does not exist a warning will be logged, but no error will be raised.      *      * @param test    a Test used to filter the classes that are discovered      * @param parent  the parent package under which classes must be in order to      *                be considered      * @param jarfile the jar file to be examined for classes      * @param stream  the inputstream of the jar file to be examined for classes      * @param urlPath the url of the jar file to be examined for classes      */
DECL|method|loadImplementationsInJar (Test test, String parent, InputStream stream, String urlPath)
specifier|private
name|void
name|loadImplementationsInJar
parameter_list|(
name|Test
name|test
parameter_list|,
name|String
name|parent
parameter_list|,
name|InputStream
name|stream
parameter_list|,
name|String
name|urlPath
parameter_list|)
block|{
name|JarInputStream
name|jarStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|jarStream
operator|=
operator|new
name|JarInputStream
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|JarEntry
name|entry
decl_stmt|;
while|while
condition|(
operator|(
name|entry
operator|=
name|jarStream
operator|.
name|getNextJarEntry
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|entry
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|entry
operator|.
name|isDirectory
argument_list|()
operator|&&
name|name
operator|.
name|startsWith
argument_list|(
name|parent
argument_list|)
operator|&&
name|name
operator|.
name|endsWith
argument_list|(
literal|".class"
argument_list|)
condition|)
block|{
name|addIfMatching
argument_list|(
name|test
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Could not search jar file '"
operator|+
name|urlPath
operator|+
literal|"' for classes matching criteria: "
operator|+
name|test
operator|+
literal|" due to an IOException: "
operator|+
name|ioe
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|ObjectHelper
operator|.
name|close
argument_list|(
name|jarStream
argument_list|,
name|urlPath
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Add the class designated by the fully qualified class name provided to      * the set of resolved classes if and only if it is approved by the Test      * supplied.      *      * @param test the test used to determine if the class matches      * @param fqn  the fully qualified name of a class      */
DECL|method|addIfMatching (Test test, String fqn)
specifier|protected
name|void
name|addIfMatching
parameter_list|(
name|Test
name|test
parameter_list|,
name|String
name|fqn
parameter_list|)
block|{
try|try
block|{
name|String
name|externalName
init|=
name|fqn
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fqn
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'.'
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ClassLoader
argument_list|>
name|set
init|=
name|getClassLoaders
argument_list|()
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|ClassLoader
name|classLoader
range|:
name|set
control|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Testing for class "
operator|+
name|externalName
operator|+
literal|" matches criteria ["
operator|+
name|test
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Class
name|type
init|=
name|classLoader
operator|.
name|loadClass
argument_list|(
name|externalName
argument_list|)
decl_stmt|;
if|if
condition|(
name|test
operator|.
name|matches
argument_list|(
name|type
argument_list|)
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Found class: "
operator|+
name|type
operator|+
literal|" in classloader: "
operator|+
name|classLoader
argument_list|)
expr_stmt|;
block|}
name|matches
operator|.
name|add
argument_list|(
operator|(
name|Class
argument_list|<
name|T
argument_list|>
operator|)
name|type
argument_list|)
expr_stmt|;
block|}
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Could not find class '"
operator|+
name|fqn
operator|+
literal|"' in classloader: "
operator|+
name|classLoader
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Could not find the class defintion '"
operator|+
name|fqn
operator|+
literal|"' in classloader: "
operator|+
name|classLoader
operator|+
literal|". Reason: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not find class '"
operator|+
name|fqn
operator|+
literal|"' in any classloaders: "
operator|+
name|set
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not examine class '"
operator|+
name|fqn
operator|+
literal|"' due to a "
operator|+
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" with message: "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

