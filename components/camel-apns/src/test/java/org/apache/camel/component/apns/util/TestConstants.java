begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.apns.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|apns
operator|.
name|util
package|;
end_package

begin_comment
comment|/**  * Interface with address and port to point inside tests  */
end_comment

begin_class
DECL|class|TestConstants
specifier|public
specifier|final
class|class
name|TestConstants
block|{
DECL|field|TEST_GATEWAY_PORT
specifier|public
specifier|static
specifier|final
name|int
name|TEST_GATEWAY_PORT
init|=
literal|7654
decl_stmt|;
DECL|field|TEST_FEEDBACK_PORT
specifier|public
specifier|static
specifier|final
name|int
name|TEST_FEEDBACK_PORT
init|=
literal|7843
decl_stmt|;
DECL|field|TEST_HOST
specifier|public
specifier|static
specifier|final
name|String
name|TEST_HOST
init|=
literal|"localhost"
decl_stmt|;
DECL|method|TestConstants ()
specifier|private
name|TestConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

