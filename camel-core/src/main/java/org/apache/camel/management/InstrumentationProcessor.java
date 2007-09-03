begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
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
name|Exchange
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
name|processor
operator|.
name|DelegateProcessor
import|;
end_import

begin_class
DECL|class|InstrumentationProcessor
specifier|public
class|class
name|InstrumentationProcessor
extends|extends
name|DelegateProcessor
block|{
DECL|field|counter
specifier|private
name|PerformanceCounter
name|counter
decl_stmt|;
DECL|method|InstrumentationProcessor (PerformanceCounter counter)
name|InstrumentationProcessor
parameter_list|(
name|PerformanceCounter
name|counter
parameter_list|)
block|{
name|this
operator|.
name|counter
operator|=
name|counter
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|startTime
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|counter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
name|counter
operator|.
name|completedExchange
argument_list|(
operator|(
name|System
operator|.
name|nanoTime
argument_list|()
operator|-
name|startTime
operator|)
operator|/
literal|1000
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|counter
operator|.
name|completedExchange
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

