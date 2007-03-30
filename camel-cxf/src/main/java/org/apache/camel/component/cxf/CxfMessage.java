begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
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
name|impl
operator|.
name|DefaultMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
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
comment|/**  * An Apache CXF {@link Message} which provides access to the underlying CXF features  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfMessage
specifier|public
class|class
name|CxfMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|cxfMessage
specifier|private
name|Message
name|cxfMessage
decl_stmt|;
DECL|method|CxfMessage ()
specifier|public
name|CxfMessage
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|MessageImpl
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|CxfMessage (Message cxfMessage)
specifier|public
name|CxfMessage
parameter_list|(
name|Message
name|cxfMessage
parameter_list|)
block|{
name|this
operator|.
name|cxfMessage
operator|=
name|cxfMessage
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|cxfMessage
operator|!=
literal|null
condition|)
block|{
return|return
literal|"CxfMessage: "
operator|+
name|cxfMessage
return|;
block|}
else|else
block|{
return|return
literal|"CxfMessage: "
operator|+
name|getBody
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getExchange ()
specifier|public
name|CxfExchange
name|getExchange
parameter_list|()
block|{
return|return
operator|(
name|CxfExchange
operator|)
name|super
operator|.
name|getExchange
argument_list|()
return|;
block|}
comment|/**      * Returns the underlying CXF message      *      * @return the CXF message      */
DECL|method|getMessage ()
specifier|public
name|Message
name|getMessage
parameter_list|()
block|{
return|return
name|cxfMessage
return|;
block|}
DECL|method|setMessage (Message cxfMessage)
specifier|public
name|void
name|setMessage
parameter_list|(
name|Message
name|cxfMessage
parameter_list|)
block|{
name|this
operator|.
name|cxfMessage
operator|=
name|cxfMessage
expr_stmt|;
block|}
DECL|method|getHeader (String name)
specifier|public
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|cxfMessage
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setHeader (String name, Object value)
specifier|public
name|void
name|setHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|cxfMessage
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getHeaders ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
name|cxfMessage
return|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|CxfMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|CxfMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createBody ()
specifier|protected
name|Object
name|createBody
parameter_list|()
block|{
return|return
name|getExchange
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|extractBodyFromCxf
argument_list|(
name|getExchange
argument_list|()
argument_list|,
name|cxfMessage
argument_list|)
return|;
block|}
block|}
end_class

end_unit

