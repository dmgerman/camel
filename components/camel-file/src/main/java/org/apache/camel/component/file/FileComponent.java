begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
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
name|util
operator|.
name|Map
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
name|CamelContext
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
name|component
operator|.
name|file
operator|.
name|strategy
operator|.
name|FileProcessStrategyFactory
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
name|spi
operator|.
name|annotations
operator|.
name|Component
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
name|FileUtil
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
name|StringHelper
import|;
end_import

begin_comment
comment|/**  *  * The<a href="http://camel.apache.org/file.html">File Component</a> provides access to file systems.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"file"
argument_list|)
annotation|@
name|FileProcessStrategy
argument_list|(
name|FileProcessStrategyFactory
operator|.
name|class
argument_list|)
DECL|class|FileComponent
specifier|public
class|class
name|FileComponent
extends|extends
name|GenericFileComponent
argument_list|<
name|File
argument_list|>
block|{
comment|/**      * GenericFile property on Camel Exchanges.      */
DECL|field|FILE_EXCHANGE_FILE
specifier|public
specifier|static
specifier|final
name|String
name|FILE_EXCHANGE_FILE
init|=
literal|"CamelFileExchangeFile"
decl_stmt|;
comment|/**      * Default camel lock filename postfix      */
DECL|field|DEFAULT_LOCK_FILE_POSTFIX
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_LOCK_FILE_POSTFIX
init|=
literal|".camelLock"
decl_stmt|;
DECL|method|FileComponent ()
specifier|public
name|FileComponent
parameter_list|()
block|{     }
DECL|method|FileComponent (CamelContext context)
specifier|public
name|FileComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|buildFileEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|GenericFileEndpoint
argument_list|<
name|File
argument_list|>
name|buildFileEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// the starting directory must be a static (not containing dynamic expressions)
if|if
condition|(
name|StringHelper
operator|.
name|hasStartToken
argument_list|(
name|remaining
argument_list|,
literal|"simple"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid directory: "
operator|+
name|remaining
operator|+
literal|". Dynamic expressions with ${ } placeholders is not allowed."
operator|+
literal|" Use the fileName option to set the dynamic expression."
argument_list|)
throw|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|FileEndpoint
name|result
init|=
operator|new
name|FileEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|result
operator|.
name|setFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|GenericFileConfiguration
name|config
init|=
operator|new
name|GenericFileConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|setDirectory
argument_list|(
name|FileUtil
operator|.
name|isAbsolute
argument_list|(
name|file
argument_list|)
condition|?
name|file
operator|.
name|getAbsolutePath
argument_list|()
else|:
name|file
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|setConfiguration
argument_list|(
name|config
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|afterPropertiesSet (GenericFileEndpoint<File> endpoint)
specifier|protected
name|void
name|afterPropertiesSet
parameter_list|(
name|GenericFileEndpoint
argument_list|<
name|File
argument_list|>
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit
