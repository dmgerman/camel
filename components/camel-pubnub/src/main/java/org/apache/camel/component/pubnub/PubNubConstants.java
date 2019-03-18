begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pubnub
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pubnub
package|;
end_package

begin_class
DECL|class|PubNubConstants
specifier|public
specifier|abstract
class|class
name|PubNubConstants
block|{
DECL|field|OPERATION
specifier|public
specifier|static
specifier|final
name|String
name|OPERATION
init|=
literal|"CamelPubNubOperation"
decl_stmt|;
DECL|field|TIMETOKEN
specifier|public
specifier|static
specifier|final
name|String
name|TIMETOKEN
init|=
literal|"CamelPubNubTimeToken"
decl_stmt|;
DECL|field|CHANNEL
specifier|public
specifier|static
specifier|final
name|String
name|CHANNEL
init|=
literal|"CamelPubNubChannel"
decl_stmt|;
DECL|field|UUID
specifier|public
specifier|static
specifier|final
name|String
name|UUID
init|=
literal|"CamelPubNubUUID"
decl_stmt|;
block|}
end_class

end_unit

