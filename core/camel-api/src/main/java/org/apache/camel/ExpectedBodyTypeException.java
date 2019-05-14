begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Thrown if the body could not be converted to the required type  */
end_comment

begin_class
DECL|class|ExpectedBodyTypeException
specifier|public
class|class
name|ExpectedBodyTypeException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|exchange
specifier|private
specifier|final
specifier|transient
name|Exchange
name|exchange
decl_stmt|;
DECL|field|expectedBodyType
specifier|private
specifier|final
specifier|transient
name|Class
argument_list|<
name|?
argument_list|>
name|expectedBodyType
decl_stmt|;
DECL|method|ExpectedBodyTypeException (Exchange exchange, Class<?> expectedBodyType)
specifier|public
name|ExpectedBodyTypeException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|expectedBodyType
parameter_list|)
block|{
name|super
argument_list|(
literal|"Could not extract IN message body as type: "
operator|+
name|expectedBodyType
operator|+
literal|" body is: "
operator|+
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|expectedBodyType
operator|=
name|expectedBodyType
expr_stmt|;
block|}
DECL|method|getExchange ()
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
DECL|method|getExpectedBodyType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getExpectedBodyType
parameter_list|()
block|{
return|return
name|expectedBodyType
return|;
block|}
block|}
end_class

end_unit

