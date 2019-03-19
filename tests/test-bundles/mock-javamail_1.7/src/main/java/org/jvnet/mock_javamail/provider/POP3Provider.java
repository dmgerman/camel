begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.jvnet.mock_javamail.provider
package|package
name|org
operator|.
name|jvnet
operator|.
name|mock_javamail
operator|.
name|provider
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Provider
import|;
end_import

begin_class
DECL|class|POP3Provider
specifier|public
class|class
name|POP3Provider
extends|extends
name|Provider
block|{
DECL|method|POP3Provider ()
specifier|public
name|POP3Provider
parameter_list|()
block|{
name|super
argument_list|(
name|Type
operator|.
name|STORE
argument_list|,
literal|"pop3"
argument_list|,
literal|"org.jvnet.mock_javamail.MockStore"
argument_list|,
literal|"Apache Software Foundation"
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

