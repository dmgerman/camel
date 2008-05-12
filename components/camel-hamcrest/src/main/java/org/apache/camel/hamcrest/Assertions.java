begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.hamcrest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|hamcrest
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|instanceOf
import|;
end_import

begin_comment
comment|/**  * A set of useful assertions you can use when testing  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|Assertions
specifier|public
class|class
name|Assertions
block|{
comment|/**      * Performs the assertion that the given value is an instance of the specified type      *      * @param value the value to be compared      * @param type  the type to assert      * @return the value cast as the type      * @throws AssertionError if the instance is not of the correct type      */
DECL|method|assertInstanceOf (Object value, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|assertInstanceOf
parameter_list|(
name|Object
name|value
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|assertThat
argument_list|(
name|value
argument_list|,
name|instanceOf
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|type
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Performs the assertion that the given value is an instance of the specified type      *      * @param message the description of the value      * @param value   the value to be compared      * @param type    the type to assert      * @return the value cast as the type      * @throws AssertionError if the instance is not of the correct type      */
DECL|method|assertInstanceOf (String message, Object value, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|assertInstanceOf
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
name|value
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|assertThat
argument_list|(
name|message
argument_list|,
name|value
argument_list|,
name|instanceOf
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|type
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

