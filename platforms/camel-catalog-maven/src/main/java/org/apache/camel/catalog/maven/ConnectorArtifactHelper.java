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
name|InputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|CatalogHelper
operator|.
name|loadText
import|;
end_import

begin_class
DECL|class|ConnectorArtifactHelper
specifier|public
specifier|final
class|class
name|ConnectorArtifactHelper
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ComponentArtifactHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ConnectorArtifactHelper ()
specifier|private
name|ConnectorArtifactHelper
parameter_list|()
block|{     }
DECL|method|loadJSonSchemas (ClassLoader classLoader)
specifier|public
specifier|static
name|String
index|[]
name|loadJSonSchemas
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|)
block|{
name|String
index|[]
name|answer
init|=
operator|new
name|String
index|[
literal|3
index|]
decl_stmt|;
name|String
name|path
init|=
literal|"camel-connector.json"
decl_stmt|;
try|try
block|{
name|InputStream
name|is
init|=
name|classLoader
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|answer
index|[
literal|0
index|]
operator|=
name|loadText
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error loading "
operator|+
name|path
operator|+
literal|" file"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|path
operator|=
literal|"camel-connector-schema.json"
expr_stmt|;
try|try
block|{
name|InputStream
name|is
init|=
name|classLoader
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|answer
index|[
literal|1
index|]
operator|=
name|loadText
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error loading "
operator|+
name|path
operator|+
literal|" file"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|path
operator|=
literal|"camel-component-schema.json"
expr_stmt|;
try|try
block|{
name|InputStream
name|is
init|=
name|classLoader
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|answer
index|[
literal|2
index|]
operator|=
name|loadText
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error loading "
operator|+
name|path
operator|+
literal|" file"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

