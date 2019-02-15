begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
package|;
end_package

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
name|Properties
import|;
end_import

begin_comment
comment|/**  * To get the version of this catalog.  */
end_comment

begin_class
DECL|class|VersionHelper
specifier|public
class|class
name|VersionHelper
block|{
DECL|field|version
specifier|private
specifier|static
specifier|volatile
name|String
name|version
decl_stmt|;
DECL|method|getVersion ()
specifier|public
specifier|synchronized
name|String
name|getVersion
parameter_list|()
block|{
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
return|return
name|version
return|;
block|}
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
comment|// try to load from maven properties first
try|try
block|{
name|Properties
name|p
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/META-INF/maven/org.apache.camel/camel-catalog/pom.properties"
argument_list|)
expr_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|version
operator|=
name|p
operator|.
name|getProperty
argument_list|(
literal|"version"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
comment|// fallback to using Java API
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
name|Package
name|aPackage
init|=
name|getClass
argument_list|()
operator|.
name|getPackage
argument_list|()
decl_stmt|;
if|if
condition|(
name|aPackage
operator|!=
literal|null
condition|)
block|{
name|version
operator|=
name|aPackage
operator|.
name|getImplementationVersion
argument_list|()
expr_stmt|;
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
name|version
operator|=
name|aPackage
operator|.
name|getSpecificationVersion
argument_list|()
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
comment|// we could not compute the version so use a blank
name|version
operator|=
literal|""
expr_stmt|;
block|}
return|return
name|version
return|;
block|}
block|}
end_class

end_unit
