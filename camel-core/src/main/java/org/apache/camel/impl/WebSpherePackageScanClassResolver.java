begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

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
name|net
operator|.
name|URL
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

begin_comment
comment|/**  * WebSphere specific resolver to handle loading annotated resources in JAR files.  */
end_comment

begin_class
DECL|class|WebSpherePackageScanClassResolver
specifier|public
class|class
name|WebSpherePackageScanClassResolver
extends|extends
name|DefaultPackageScanClassResolver
block|{
DECL|field|resourcePath
specifier|private
specifier|final
name|String
name|resourcePath
decl_stmt|;
comment|/**      * Constructor.      *      * @param resourcePath  the fixed resource path to use for fetching camel jars in WebSphere.      */
DECL|method|WebSpherePackageScanClassResolver (String resourcePath)
specifier|public
name|WebSpherePackageScanClassResolver
parameter_list|(
name|String
name|resourcePath
parameter_list|)
block|{
name|this
operator|.
name|resourcePath
operator|=
name|resourcePath
expr_stmt|;
block|}
comment|/**      * Is the classloader from IBM and thus the WebSphere platform?      *      * @param loader  the classloader      * @return<tt>true</tt> if IBM classloader,<tt>false</tt> otherwise.      */
DECL|method|isWebSphereClassLoader (ClassLoader loader)
specifier|public
specifier|static
name|boolean
name|isWebSphereClassLoader
parameter_list|(
name|ClassLoader
name|loader
parameter_list|)
block|{
return|return
name|loader
operator|!=
literal|null
condition|?
name|loader
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"com.ibm"
argument_list|)
else|:
literal|false
return|;
block|}
comment|/**      * Overloaded to handle specific problem with getting resources on the IBM WebSphere platform.      *<p/>      * WebSphere can<b>not</b> load resources if the resource to load is a folder name, such as a      * packagename, you have to explicit name a resource that is a file.      *      * @param loader  the classloader      * @param packageName   the packagename for the package to load      * @return  URL's for the given package      * @throws java.io.IOException is thrown by the classloader      */
annotation|@
name|Override
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
comment|// try super first, just in vase
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|enumeration
init|=
name|super
operator|.
name|getResources
argument_list|(
name|loader
argument_list|,
name|packageName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|enumeration
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Using WebSphere workaround to load the camel jars with the annotated converters."
argument_list|)
expr_stmt|;
comment|// Special WebSphere trick to load a file that exists in the JAR and then let it go from there.
comment|// The trick is that we just need the URL's for the .jars that contains the type
comment|// converters that is annotated. So by searching for this resource WebSphere is able to find
comment|// it and return the URL to the .jar file with the resource. Then the DefaultPackageScanClassResolver
comment|// can take it from there and find the classes that are annotated.
name|enumeration
operator|=
name|loader
operator|.
name|getResources
argument_list|(
name|resourcePath
argument_list|)
expr_stmt|;
block|}
return|return
name|enumeration
return|;
block|}
block|}
end_class

end_unit

