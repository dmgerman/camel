begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|InvalidPayloadException
specifier|public
class|class
name|InvalidPayloadException
extends|extends
name|CamelExchangeException
block|{
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|method|InvalidPayloadException (Exchange exchange, Class<?> type)
specifier|public
name|InvalidPayloadException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|super
argument_list|(
literal|"No in body available of type: "
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
name|NoSuchPropertyException
operator|.
name|valueDescription
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * The expected type of the body      */
DECL|method|getType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
block|}
end_class

end_unit

