begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.api.utils
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|SerializationFeature
import|;
end_import

begin_comment
comment|/**  * Factory class for creating {@linkplain com.fasterxml.jackson.databind.ObjectMapper}  */
end_comment

begin_class
DECL|class|JsonUtils
specifier|public
specifier|abstract
class|class
name|JsonUtils
block|{
DECL|method|createObjectMapper ()
specifier|public
specifier|static
name|ObjectMapper
name|createObjectMapper
parameter_list|()
block|{
comment|// enable date time support including Java 1.8 ZonedDateTime
name|ObjectMapper
name|objectMapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|objectMapper
operator|.
name|configure
argument_list|(
name|SerializationFeature
operator|.
name|WRITE_DATES_AS_TIMESTAMPS
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|objectMapper
operator|.
name|registerModule
argument_list|(
operator|new
name|DateTimeModule
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|objectMapper
return|;
block|}
block|}
end_class

end_unit

