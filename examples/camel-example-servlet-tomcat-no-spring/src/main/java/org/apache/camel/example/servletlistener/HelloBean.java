begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.servletlistener
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|servletlistener
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
name|Header
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
name|language
operator|.
name|Simple
import|;
end_import

begin_class
DECL|class|HelloBean
specifier|public
class|class
name|HelloBean
block|{
DECL|method|sayHello (@eaderR) String name, @Simple(R) String country)
specifier|public
name|String
name|sayHello
parameter_list|(
annotation|@
name|Header
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|,
annotation|@
name|Simple
argument_list|(
literal|"${sys.user.country}"
argument_list|)
name|String
name|country
parameter_list|)
block|{
return|return
literal|"Hello "
operator|+
name|name
operator|+
literal|", how are you? You are from: "
operator|+
name|country
return|;
block|}
block|}
end_class

end_unit

