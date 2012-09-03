begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|Consume
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
name|Produce
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
name|ProducerTemplate
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Test consume annotation  */
end_comment

begin_class
DECL|class|ConsumeTest
specifier|public
class|class
name|ConsumeTest
extends|extends
name|CdiTestSupport
block|{
annotation|@
name|Inject
annotation|@
name|Mock
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"seda:start"
argument_list|)
DECL|field|producer
specifier|private
name|ProducerTemplate
name|producer
decl_stmt|;
annotation|@
name|Consume
argument_list|(
name|uri
operator|=
literal|"seda:start"
argument_list|)
DECL|method|onStart (String body)
specifier|public
name|void
name|onStart
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|producer
operator|.
name|sendBody
argument_list|(
literal|"mock:result"
argument_list|,
literal|"Hello "
operator|+
name|body
operator|+
literal|"!"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|consumeAnnotation ()
specifier|public
name|void
name|consumeAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
literal|"Could not inject producer"
argument_list|,
name|producer
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not inject mock endpoint"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello world!"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
literal|"world"
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

