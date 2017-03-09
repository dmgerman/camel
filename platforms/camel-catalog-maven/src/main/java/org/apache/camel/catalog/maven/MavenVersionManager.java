begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|maven
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|grape
operator|.
name|Grape
import|;
end_import

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|GroovyClassLoader
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
name|catalog
operator|.
name|VersionManager
import|;
end_import

begin_comment
comment|/**  * A {@link VersionManager} that can load the resources using Maven to download needed artifacts from  * a local or remote Maven repository.  *<p/>  * This implementation uses Groovy Grape to download the Maven JARs.  */
end_comment

begin_class
DECL|class|MavenVersionManager
specifier|public
class|class
name|MavenVersionManager
implements|implements
name|VersionManager
block|{
DECL|field|classLoader
specifier|private
specifier|final
name|ClassLoader
name|classLoader
init|=
operator|new
name|GroovyClassLoader
argument_list|()
decl_stmt|;
DECL|field|version
specifier|private
name|String
name|version
decl_stmt|;
DECL|field|runtimeProviderVersion
specifier|private
name|String
name|runtimeProviderVersion
decl_stmt|;
DECL|field|cacheDirectory
specifier|private
name|String
name|cacheDirectory
decl_stmt|;
comment|/**      * Configures the directory for the download cache.      *<p/>      * The default folder is<tt>USER_HOME/.groovy/grape</tt>      *      * @param directory the directory.      */
DECL|method|setCacheDirectory (String directory)
specifier|public
name|void
name|setCacheDirectory
parameter_list|(
name|String
name|directory
parameter_list|)
block|{
name|this
operator|.
name|cacheDirectory
operator|=
name|directory
expr_stmt|;
block|}
comment|/**      * To add a 3rd party Maven repository.      *      * @param name the repository name      * @param url  the repository url      */
DECL|method|addMavenRepository (String name, String url)
specifier|public
name|void
name|addMavenRepository
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|repo
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|repo
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|repo
operator|.
name|put
argument_list|(
literal|"root"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|Grape
operator|.
name|addResolver
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLoadedVersion ()
specifier|public
name|String
name|getLoadedVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
annotation|@
name|Override
DECL|method|loadVersion (String version)
specifier|public
name|boolean
name|loadVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|cacheDirectory
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"grape.root"
argument_list|,
name|cacheDirectory
argument_list|)
expr_stmt|;
block|}
name|Grape
operator|.
name|setEnableAutoDownload
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|param
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|param
operator|.
name|put
argument_list|(
literal|"classLoader"
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
name|param
operator|.
name|put
argument_list|(
literal|"group"
argument_list|,
literal|"org.apache.camel"
argument_list|)
expr_stmt|;
name|param
operator|.
name|put
argument_list|(
literal|"module"
argument_list|,
literal|"camel-catalog"
argument_list|)
expr_stmt|;
name|param
operator|.
name|put
argument_list|(
literal|"version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|Grape
operator|.
name|grab
argument_list|(
name|param
argument_list|)
expr_stmt|;
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getRuntimeProviderLoadedVersion ()
specifier|public
name|String
name|getRuntimeProviderLoadedVersion
parameter_list|()
block|{
return|return
name|runtimeProviderVersion
return|;
block|}
annotation|@
name|Override
DECL|method|loadRuntimeProviderVersion (String groupId, String artifactId, String version)
specifier|public
name|boolean
name|loadRuntimeProviderVersion
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
try|try
block|{
name|Grape
operator|.
name|setEnableAutoDownload
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|param
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|param
operator|.
name|put
argument_list|(
literal|"classLoader"
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
name|param
operator|.
name|put
argument_list|(
literal|"group"
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
name|param
operator|.
name|put
argument_list|(
literal|"module"
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
name|param
operator|.
name|put
argument_list|(
literal|"version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|Grape
operator|.
name|grab
argument_list|(
name|param
argument_list|)
expr_stmt|;
name|this
operator|.
name|runtimeProviderVersion
operator|=
name|version
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getResourceAsStream (String name)
specifier|public
name|InputStream
name|getResourceAsStream
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|runtimeProviderVersion
operator|!=
literal|null
condition|)
block|{
name|is
operator|=
name|doGetResourceAsStream
argument_list|(
name|name
argument_list|,
name|runtimeProviderVersion
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|is
operator|==
literal|null
operator|&&
name|version
operator|!=
literal|null
condition|)
block|{
name|is
operator|=
name|doGetResourceAsStream
argument_list|(
name|name
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
return|return
name|is
return|;
block|}
DECL|method|doGetResourceAsStream (String name, String version)
specifier|private
name|InputStream
name|doGetResourceAsStream
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|version
parameter_list|)
block|{
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|URL
name|found
init|=
literal|null
decl_stmt|;
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|urls
init|=
name|classLoader
operator|.
name|getResources
argument_list|(
name|name
argument_list|)
decl_stmt|;
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
name|urls
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|url
operator|.
name|getPath
argument_list|()
operator|.
name|contains
argument_list|(
name|version
argument_list|)
condition|)
block|{
name|found
operator|=
name|url
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|found
operator|!=
literal|null
condition|)
block|{
return|return
name|found
operator|.
name|openStream
argument_list|()
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

