begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs.testbean
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
operator|.
name|jaxrs
operator|.
name|testbean
package|;
end_package

begin_comment
comment|// START SNIPPET: example
end_comment

begin_interface
DECL|interface|CustomerServiceNoAnnotations
specifier|public
interface|interface
name|CustomerServiceNoAnnotations
block|{
DECL|method|getCustomer (String id)
name|Customer
name|getCustomer
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
block|}
end_interface

begin_comment
comment|// END SNIPPET: example
end_comment

end_unit

