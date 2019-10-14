begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.debezium.configuration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|debezium
operator|.
name|configuration
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|io
operator|.
name|debezium
operator|.
name|config
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|connect
operator|.
name|file
operator|.
name|FileStreamSourceConnector
import|;
end_import

begin_class
DECL|class|FileConnectorEmbeddedDebeziumConfiguration
specifier|public
class|class
name|FileConnectorEmbeddedDebeziumConfiguration
extends|extends
name|MySqlConnectorEmbeddedDebeziumConfiguration
block|{
DECL|field|testFilePath
specifier|private
name|Path
name|testFilePath
decl_stmt|;
DECL|field|topicConfig
specifier|private
name|String
name|topicConfig
decl_stmt|;
annotation|@
name|Override
DECL|method|createConnectorConfiguration ()
specifier|protected
name|Configuration
name|createConnectorConfiguration
parameter_list|()
block|{
return|return
name|Configuration
operator|.
name|create
argument_list|()
operator|.
name|with
argument_list|(
name|FileStreamSourceConnector
operator|.
name|FILE_CONFIG
argument_list|,
name|testFilePath
argument_list|)
operator|.
name|with
argument_list|(
name|FileStreamSourceConnector
operator|.
name|TOPIC_CONFIG
argument_list|,
name|topicConfig
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|validateConnectorConfiguration ()
specifier|protected
name|ConfigurationValidation
name|validateConnectorConfiguration
parameter_list|()
block|{
if|if
condition|(
name|isFieldValueNotSet
argument_list|(
name|testFilePath
argument_list|)
condition|)
block|{
return|return
name|ConfigurationValidation
operator|.
name|notValid
argument_list|(
literal|"testFilePath is not set"
argument_list|)
return|;
block|}
if|if
condition|(
name|isFieldValueNotSet
argument_list|(
name|topicConfig
argument_list|)
condition|)
block|{
return|return
name|ConfigurationValidation
operator|.
name|notValid
argument_list|(
literal|"topicConfig is not set"
argument_list|)
return|;
block|}
return|return
name|ConfigurationValidation
operator|.
name|valid
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|configureConnectorClass ()
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|configureConnectorClass
parameter_list|()
block|{
return|return
name|FileStreamSourceConnector
operator|.
name|class
return|;
block|}
DECL|method|getTestFilePath ()
specifier|public
name|Path
name|getTestFilePath
parameter_list|()
block|{
return|return
name|testFilePath
return|;
block|}
DECL|method|setTestFilePath (Path testFilePath)
specifier|public
name|void
name|setTestFilePath
parameter_list|(
name|Path
name|testFilePath
parameter_list|)
block|{
name|this
operator|.
name|testFilePath
operator|=
name|testFilePath
expr_stmt|;
block|}
DECL|method|getTopicConfig ()
specifier|public
name|String
name|getTopicConfig
parameter_list|()
block|{
return|return
name|topicConfig
return|;
block|}
DECL|method|setTopicConfig (String topicConfig)
specifier|public
name|void
name|setTopicConfig
parameter_list|(
name|String
name|topicConfig
parameter_list|)
block|{
name|this
operator|.
name|topicConfig
operator|=
name|topicConfig
expr_stmt|;
block|}
block|}
end_class

end_unit

