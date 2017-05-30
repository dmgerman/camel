begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|camel
operator|.
name|impl
operator|.
name|DefaultPackageScanClassResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|IOHelper
import|;
end_import

begin_comment
comment|/**  * An implementation of the {@code org.apache.camel.spi.PackageScanClassResolver} that is able to  * scan spring-boot fat jars to find classes contained also in nested jars.  */
end_comment

begin_class
DECL|class|FatJarPackageScanClassResolver
specifier|public
class|class
name|FatJarPackageScanClassResolver
extends|extends
name|DefaultPackageScanClassResolver
block|{
DECL|field|SPRING_BOOT_CLASSIC_LIB_ROOT
specifier|private
specifier|static
specifier|final
name|String
name|SPRING_BOOT_CLASSIC_LIB_ROOT
init|=
literal|"lib/"
decl_stmt|;
DECL|field|SPRING_BOOT_BOOT_INF_LIB_ROOT
specifier|private
specifier|static
specifier|final
name|String
name|SPRING_BOOT_BOOT_INF_LIB_ROOT
init|=
literal|"BOOT-INF/lib/"
decl_stmt|;
DECL|field|SPRING_BOOT_BOOT_INF_CLASSES_ROOT
specifier|private
specifier|static
specifier|final
name|String
name|SPRING_BOOT_BOOT_INF_CLASSES_ROOT
init|=
literal|"BOOT-INF/classes/"
decl_stmt|;
comment|/**      * Loads all the class entries from the main JAR and all nested jars.      *      * @param stream  the inputstream of the jar file to be examined for classes      * @param urlPath the url of the jar file to be examined for classes      * @return all the .class entries from the main JAR and all nested jars      */
annotation|@
name|Override
DECL|method|doLoadJarClassEntries (InputStream stream, String urlPath)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|doLoadJarClassEntries
parameter_list|(
name|InputStream
name|stream
parameter_list|,
name|String
name|urlPath
parameter_list|)
block|{
return|return
name|doLoadJarClassEntries
argument_list|(
name|stream
argument_list|,
name|urlPath
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|doLoadJarClassEntries (InputStream stream, String urlPath, boolean inspectNestedJars, boolean closeStream)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|doLoadJarClassEntries
parameter_list|(
name|InputStream
name|stream
parameter_list|,
name|String
name|urlPath
parameter_list|,
name|boolean
name|inspectNestedJars
parameter_list|,
name|boolean
name|closeStream
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|entries
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
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
name|endsWith
argument_list|(
literal|".class"
argument_list|)
condition|)
block|{
name|entries
operator|.
name|add
argument_list|(
name|cleanupSpringbootClassName
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|inspectNestedJars
operator|&&
operator|!
name|entry
operator|.
name|isDirectory
argument_list|()
operator|&&
name|isSpringBootNestedJar
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|String
name|nestedUrl
init|=
name|urlPath
operator|+
literal|"!/"
operator|+
name|name
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Inspecting nested jar: {}"
argument_list|,
name|nestedUrl
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|nestedEntries
init|=
name|doLoadJarClassEntries
argument_list|(
name|jarStream
argument_list|,
name|nestedUrl
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|entries
operator|.
name|addAll
argument_list|(
name|nestedEntries
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
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot search jar file '"
operator|+
name|urlPath
operator|+
literal|" due to an IOException: "
operator|+
name|ioe
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception is ignored."
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|closeStream
condition|)
block|{
comment|// stream is left open when scanning nested jars, otherwise the fat jar stream gets closed
name|IOHelper
operator|.
name|close
argument_list|(
name|jarStream
argument_list|,
name|urlPath
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|entries
return|;
block|}
DECL|method|isSpringBootNestedJar (String name)
specifier|private
name|boolean
name|isSpringBootNestedJar
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// Supporting both versions of the packaging model
return|return
name|name
operator|.
name|endsWith
argument_list|(
literal|".jar"
argument_list|)
operator|&&
operator|(
name|name
operator|.
name|startsWith
argument_list|(
name|SPRING_BOOT_CLASSIC_LIB_ROOT
argument_list|)
operator|||
name|name
operator|.
name|startsWith
argument_list|(
name|SPRING_BOOT_BOOT_INF_LIB_ROOT
argument_list|)
operator|)
return|;
block|}
DECL|method|cleanupSpringbootClassName (String name)
specifier|private
name|String
name|cleanupSpringbootClassName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// Classes inside BOOT-INF/classes will be loaded by the new classloader as if they were in the root
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
name|SPRING_BOOT_BOOT_INF_CLASSES_ROOT
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
name|SPRING_BOOT_BOOT_INF_CLASSES_ROOT
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

