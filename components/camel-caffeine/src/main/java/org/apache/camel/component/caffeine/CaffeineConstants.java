begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.caffeine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|caffeine
package|;
end_package

begin_interface
DECL|interface|CaffeineConstants
specifier|public
interface|interface
name|CaffeineConstants
block|{
DECL|field|ACTION
name|String
name|ACTION
init|=
literal|"CamelCaffeineAction"
decl_stmt|;
DECL|field|ACTION_HAS_RESULT
name|String
name|ACTION_HAS_RESULT
init|=
literal|"CamelCaffeineActionHasResult"
decl_stmt|;
DECL|field|ACTION_SUCCEEDED
name|String
name|ACTION_SUCCEEDED
init|=
literal|"CamelCaffeineActionSucceeded"
decl_stmt|;
DECL|field|KEY
name|String
name|KEY
init|=
literal|"CamelCaffeineKey"
decl_stmt|;
DECL|field|KEYS
name|String
name|KEYS
init|=
literal|"CamelCaffeineKeys"
decl_stmt|;
DECL|field|VALUE
name|String
name|VALUE
init|=
literal|"CamelCaffeineValue"
decl_stmt|;
DECL|field|OLD_VALUE
name|String
name|OLD_VALUE
init|=
literal|"CamelCaffeineOldValue"
decl_stmt|;
DECL|field|ACTION_CLEANUP
name|String
name|ACTION_CLEANUP
init|=
literal|"CLEANUP"
decl_stmt|;
DECL|field|ACTION_PUT
name|String
name|ACTION_PUT
init|=
literal|"PUT"
decl_stmt|;
DECL|field|ACTION_PUT_ALL
name|String
name|ACTION_PUT_ALL
init|=
literal|"PUT_ALL"
decl_stmt|;
DECL|field|ACTION_GET
name|String
name|ACTION_GET
init|=
literal|"GET"
decl_stmt|;
DECL|field|ACTION_GET_ALL
name|String
name|ACTION_GET_ALL
init|=
literal|"GET_ALL"
decl_stmt|;
DECL|field|ACTION_INVALIDATE
name|String
name|ACTION_INVALIDATE
init|=
literal|"INVALIDATE"
decl_stmt|;
DECL|field|ACTION_INVALIDATE_ALL
name|String
name|ACTION_INVALIDATE_ALL
init|=
literal|"INVALIDATE_ALL"
decl_stmt|;
block|}
end_interface

end_unit

