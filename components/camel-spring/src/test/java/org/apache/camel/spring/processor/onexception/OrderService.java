begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.processor.onexception
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|processor
operator|.
name|onexception
package|;
end_package

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
name|Body
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
name|Headers
import|;
end_import

begin_comment
comment|/**  * Order service as a plain POJO class  */
end_comment

begin_class
DECL|class|OrderService
specifier|public
class|class
name|OrderService
block|{
comment|/**      * This method handle our order input and return the order      *       * @param headers the in headers      * @param payload the in payload      * @return the out payload      * @throws OrderFailedException is thrown if the order cannot be processed      */
DECL|method|handleOrder (@eaders Map<String, Object> headers, @Body String payload)
specifier|public
name|Object
name|handleOrder
parameter_list|(
annotation|@
name|Headers
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
annotation|@
name|Body
name|String
name|payload
parameter_list|)
throws|throws
name|OrderFailedException
block|{
if|if
condition|(
literal|"Order: kaboom"
operator|.
name|equals
argument_list|(
name|payload
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OrderFailedException
argument_list|(
literal|"Cannot order: kaboom"
argument_list|)
throw|;
block|}
else|else
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"orderid"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
return|return
literal|"Order OK"
return|;
block|}
block|}
comment|/**      * This method creates the response to the caller if the order could not be      * processed      *       * @param headers the in headers      * @param payload the in payload      * @return the out payload      */
DECL|method|orderFailed (@eaders Map<String, Object> headers, @Body String payload)
specifier|public
name|Object
name|orderFailed
parameter_list|(
annotation|@
name|Headers
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
annotation|@
name|Body
name|String
name|payload
parameter_list|)
block|{
name|headers
operator|.
name|put
argument_list|(
literal|"orderid"
argument_list|,
literal|"failed"
argument_list|)
expr_stmt|;
return|return
literal|"Order ERROR"
return|;
block|}
block|}
end_class

end_unit

