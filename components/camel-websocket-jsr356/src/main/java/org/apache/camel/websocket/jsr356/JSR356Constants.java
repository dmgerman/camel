begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.websocket.jsr356
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|websocket
operator|.
name|jsr356
package|;
end_package

begin_interface
DECL|interface|JSR356Constants
specifier|public
interface|interface
name|JSR356Constants
block|{
DECL|field|SESSION
name|String
name|SESSION
init|=
literal|"jsr356.session"
decl_stmt|;
DECL|field|USE_INCOMING_SESSION
name|String
name|USE_INCOMING_SESSION
init|=
literal|"jsr356.producer.session.incoming.use"
decl_stmt|;
block|}
end_interface

end_unit

