begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.tx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|tx
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|processor
operator|.
name|SpringTestHelper
operator|.
name|createSpringCamelContext
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
name|ContextTestSupport
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
name|Processor
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
name|Route
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
name|impl
operator|.
name|EventDrivenConsumerRoute
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
name|DeadLetterChannel
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
name|DelegateAsyncProcessor
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
name|Pipeline
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|log4j
operator|.
name|Logger
import|;
end_import

begin_comment
comment|/**  * Test case derived from:  *      http://activemq.apache.org/camel/transactional-client.html  *  and  *      Martin Krasser's sample: http://www.nabble.com/JMS-Transactions---How-To-td15168958s22882.html#a15198803  *  * @author Kevin Ross  *  */
end_comment

begin_class
DECL|class|AbstractTransactionTest
specifier|public
specifier|abstract
class|class
name|AbstractTransactionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|log
specifier|private
name|Logger
name|log
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
comment|// keep a ref to easily check the count at the end.
comment|//    private ConditionalExceptionProcessor conditionalExceptionProcessor;
comment|//    Policy required = new SpringTransactionPolicy( bean( TransactionTemplate.class, "PROPAGATION_REQUIRED" ) );
comment|//    Policy notSupported = new SpringTransactionPolicy( bean( TransactionTemplate.class, "PROPAGATION_NOT_SUPPORTED" ) );
comment|//    Policy requireNew = new SpringTransactionPolicy( bean( TransactionTemplate.class, "PROPAGATION_REQUIRES_NEW" ) );
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|//        setConditionalExceptionProcessor( new ConditionalExceptionProcessor() );
block|}
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|setCamelContextService
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|context
operator|=
literal|null
expr_stmt|;
name|template
operator|=
literal|null
expr_stmt|;
comment|//        setConditionalExceptionProcessor( null );
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|createSpringCamelContext
argument_list|(
name|this
argument_list|,
literal|"org/apache/camel/component/jms/tx/JavaDSLTransactionTest.xml"
argument_list|)
return|;
block|}
DECL|method|assertResult ()
specifier|protected
name|void
name|assertResult
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"activemq:queue:foo"
argument_list|,
literal|"blah"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000L
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expected only 2 calls to process() (1 failure, 1 success) but encountered "
operator|+
name|getConditionalExceptionProcessor
argument_list|()
operator|.
name|getCount
argument_list|()
operator|+
literal|"."
argument_list|,
name|getConditionalExceptionProcessor
argument_list|()
operator|.
name|getCount
argument_list|()
operator|<=
literal|2
argument_list|)
expr_stmt|;
block|}
DECL|method|getConditionalExceptionProcessor ()
specifier|protected
name|ConditionalExceptionProcessor
name|getConditionalExceptionProcessor
parameter_list|()
block|{
name|Route
name|route
init|=
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|route
argument_list|)
expr_stmt|;
return|return
name|getConditionalExceptionProcessor
argument_list|(
name|route
argument_list|)
return|;
block|}
comment|/**      * By default routes should be wrapped in the {@link DeadLetterChannel} so      * lets unwrap that and return the actual processor      */
DECL|method|getConditionalExceptionProcessor ( Route route )
specifier|protected
name|ConditionalExceptionProcessor
name|getConditionalExceptionProcessor
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
comment|//
comment|// the following is very specific (and brittle) and is not generally useful outside these transaction tests (nor intended to be).
comment|//
name|EventDrivenConsumerRoute
name|consumerRoute
init|=
name|assertIsInstanceOf
argument_list|(
name|EventDrivenConsumerRoute
operator|.
name|class
argument_list|,
name|route
argument_list|)
decl_stmt|;
name|Processor
name|processor
init|=
name|findProcessorByClass
argument_list|(
name|consumerRoute
operator|.
name|getProcessor
argument_list|()
argument_list|,
name|ConditionalExceptionProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|assertIsInstanceOf
argument_list|(
name|ConditionalExceptionProcessor
operator|.
name|class
argument_list|,
name|processor
argument_list|)
return|;
block|}
comment|/**      * Find the first instance of a Processor of a given class.      *      * @param processor      * @param findClass      * @return      */
DECL|method|findProcessorByClass ( Processor processor, Class findClass )
specifier|protected
name|Processor
name|findProcessorByClass
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|Class
name|findClass
parameter_list|)
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|processor
operator|=
name|unwrapDeadLetter
argument_list|(
name|processor
argument_list|)
expr_stmt|;
if|if
condition|(
name|processor
operator|instanceof
name|DelegateAsyncProcessor
condition|)
block|{
name|processor
operator|=
operator|(
operator|(
name|DelegateAsyncProcessor
operator|)
name|processor
operator|)
operator|.
name|getProcessor
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|DelegateProcessor
condition|)
block|{
comment|// TransactionInterceptor is a DelegateProcessor
name|processor
operator|=
operator|(
operator|(
name|DelegateProcessor
operator|)
name|processor
operator|)
operator|.
name|getProcessor
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|Pipeline
condition|)
block|{
for|for
control|(
name|Processor
name|p
range|:
operator|(
operator|(
name|Pipeline
operator|)
name|processor
operator|)
operator|.
name|getProcessors
argument_list|()
control|)
block|{
name|p
operator|=
name|findProcessorByClass
argument_list|(
name|p
argument_list|,
name|findClass
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
operator|&&
name|p
operator|.
name|getClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|findClass
argument_list|)
condition|)
block|{
name|processor
operator|=
name|p
expr_stmt|;
return|return
name|processor
return|;
block|}
block|}
block|}
else|else
block|{
return|return
name|processor
return|;
block|}
block|}
block|}
DECL|method|unwrapDeadLetter ( Processor processor )
specifier|private
name|Processor
name|unwrapDeadLetter
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
if|if
condition|(
name|processor
operator|instanceof
name|DeadLetterChannel
condition|)
block|{
name|processor
operator|=
operator|(
operator|(
name|DeadLetterChannel
operator|)
name|processor
operator|)
operator|.
name|getOutput
argument_list|()
expr_stmt|;
block|}
return|return
name|processor
return|;
block|}
block|}
end_class

end_unit

