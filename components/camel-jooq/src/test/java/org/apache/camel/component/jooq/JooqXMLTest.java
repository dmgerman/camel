begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jooq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jooq
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
name|CamelContext
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
name|ExchangePattern
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
name|jooq
operator|.
name|UpdatableRecord
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_comment
comment|/**  * Only for testing XML DSL. All basic tests are located here: {@link JooqProducerTest}, {@link JooqConsumerTest}.  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"/jooq-spring.xml"
block|,
literal|"/camel-context.xml"
block|}
argument_list|)
DECL|class|JooqXMLTest
specifier|public
class|class
name|JooqXMLTest
extends|extends
name|BaseJooqTest
block|{
annotation|@
name|Autowired
DECL|field|context
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Test
DECL|method|testInsert ()
specifier|public
name|void
name|testInsert
parameter_list|()
block|{
name|ProducerTemplate
name|producerTemplate
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|UpdatableRecord
name|entity
init|=
operator|(
name|UpdatableRecord
operator|)
name|producerTemplate
operator|.
name|sendBody
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:insert"
argument_list|)
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
literal|"empty"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExecute ()
specifier|public
name|void
name|testExecute
parameter_list|()
block|{
name|ProducerTemplate
name|producerTemplate
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|producerTemplate
operator|.
name|sendBody
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:execute"
argument_list|)
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
literal|"empty"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFetch ()
specifier|public
name|void
name|testFetch
parameter_list|()
block|{
name|ProducerTemplate
name|producerTemplate
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|producerTemplate
operator|.
name|sendBody
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:fetch"
argument_list|)
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
literal|"empty"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
