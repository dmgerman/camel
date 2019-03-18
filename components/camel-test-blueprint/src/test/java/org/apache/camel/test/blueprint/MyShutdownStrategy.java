begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|blueprint
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
name|impl
operator|.
name|DefaultShutdownStrategy
import|;
end_import

begin_class
DECL|class|MyShutdownStrategy
specifier|public
class|class
name|MyShutdownStrategy
extends|extends
name|DefaultShutdownStrategy
block|{
annotation|@
name|Override
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
comment|// only accept if its 5, as camel-test-blueprint sets a default timeout to 10 sec
comment|// and we want to unit test that we can change that to 5 seconds
if|if
condition|(
name|timeout
operator|==
literal|5
condition|)
block|{
name|super
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

