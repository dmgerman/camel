begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sap.netweaver
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sap
operator|.
name|netweaver
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|DefaultEndpoint
import|;
end_import

begin_class
DECL|class|NetWeaverEndpoint
specifier|public
class|class
name|NetWeaverEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|json
specifier|private
name|boolean
name|json
decl_stmt|;
DECL|field|jsonAsMap
specifier|private
name|boolean
name|jsonAsMap
decl_stmt|;
DECL|field|flatternMap
specifier|private
name|boolean
name|flatternMap
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|method|NetWeaverEndpoint (String endpointUri, Component component)
specifier|public
name|NetWeaverEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|NetWeaverProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Consumer is not supported"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
DECL|method|setUrl (String url)
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|isJson ()
specifier|public
name|boolean
name|isJson
parameter_list|()
block|{
return|return
name|json
return|;
block|}
DECL|method|setJson (boolean json)
specifier|public
name|void
name|setJson
parameter_list|(
name|boolean
name|json
parameter_list|)
block|{
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
block|}
DECL|method|isJsonAsMap ()
specifier|public
name|boolean
name|isJsonAsMap
parameter_list|()
block|{
return|return
name|jsonAsMap
return|;
block|}
DECL|method|setJsonAsMap (boolean jsonAsMap)
specifier|public
name|void
name|setJsonAsMap
parameter_list|(
name|boolean
name|jsonAsMap
parameter_list|)
block|{
name|this
operator|.
name|jsonAsMap
operator|=
name|jsonAsMap
expr_stmt|;
block|}
DECL|method|isFlatternMap ()
specifier|public
name|boolean
name|isFlatternMap
parameter_list|()
block|{
return|return
name|flatternMap
return|;
block|}
DECL|method|setFlatternMap (boolean flatternMap)
specifier|public
name|void
name|setFlatternMap
parameter_list|(
name|boolean
name|flatternMap
parameter_list|)
block|{
name|this
operator|.
name|flatternMap
operator|=
name|flatternMap
expr_stmt|;
block|}
block|}
end_class

end_unit

