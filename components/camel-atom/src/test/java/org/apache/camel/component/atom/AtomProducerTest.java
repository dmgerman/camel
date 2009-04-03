begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atom
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atom
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
name|ContextTestSupport
import|;
end_import

begin_comment
comment|/**  * Unit test for AtomProducer.  */
end_comment

begin_class
DECL|class|AtomProducerTest
specifier|public
class|class
name|AtomProducerTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testNotYetImplemented ()
specifier|public
name|void
name|testNotYetImplemented
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"atom:file://target/out.atom"
argument_list|)
operator|.
name|createProducer
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// ok
block|}
block|}
block|}
end_class

end_unit

