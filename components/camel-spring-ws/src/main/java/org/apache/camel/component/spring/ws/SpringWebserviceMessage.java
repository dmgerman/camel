begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
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
name|support
operator|.
name|DefaultMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|WebServiceMessage
import|;
end_import

begin_class
DECL|class|SpringWebserviceMessage
specifier|public
class|class
name|SpringWebserviceMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|webServiceMessage
specifier|private
name|WebServiceMessage
name|webServiceMessage
decl_stmt|;
DECL|method|SpringWebserviceMessage (CamelContext camelContext, WebServiceMessage webServiceMessage)
specifier|public
name|SpringWebserviceMessage
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|WebServiceMessage
name|webServiceMessage
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|webServiceMessage
operator|=
name|webServiceMessage
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createBody ()
specifier|protected
name|Object
name|createBody
parameter_list|()
block|{
if|if
condition|(
name|webServiceMessage
operator|!=
literal|null
condition|)
block|{
return|return
name|webServiceMessage
operator|.
name|getPayloadSource
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getWebServiceMessage ()
specifier|public
name|WebServiceMessage
name|getWebServiceMessage
parameter_list|()
block|{
return|return
name|webServiceMessage
return|;
block|}
DECL|method|setWebServiceMessage (WebServiceMessage webServiceMessage)
specifier|public
name|void
name|setWebServiceMessage
parameter_list|(
name|WebServiceMessage
name|webServiceMessage
parameter_list|)
block|{
name|this
operator|.
name|webServiceMessage
operator|=
name|webServiceMessage
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|DefaultMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|SpringWebserviceMessage
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|webServiceMessage
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
literal|"SpringWebserviceMessage["
operator|+
name|webServiceMessage
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

