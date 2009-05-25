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

begin_comment
comment|/**  * File component.  */
end_comment

begin_class
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
DECL|method|buildFileEndpoint (String uri, String remaining, Map parameters)
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
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
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

