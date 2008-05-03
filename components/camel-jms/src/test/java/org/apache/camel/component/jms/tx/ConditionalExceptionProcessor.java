begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Processor
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
comment|/**  * Conditionally throws exception causing a rollback  *  * @author Kevin Ross  */
end_comment

begin_class
DECL|class|ConditionalExceptionProcessor
specifier|public
class|class
name|ConditionalExceptionProcessor
implements|implements
name|Processor
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
DECL|field|count
specifier|private
name|int
name|count
decl_stmt|;
DECL|method|ConditionalExceptionProcessor ()
specifier|public
name|ConditionalExceptionProcessor
parameter_list|()
block|{              }
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
name|setCount
argument_list|(
name|getCount
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
comment|// System.out.println(this + "; getCount() = " + getCount());
name|AbstractTransactionTest
operator|.
name|assertTrue
argument_list|(
literal|"Expected only 2 calls to process() but encountered "
operator|+
name|getCount
argument_list|()
operator|+
literal|".  There should be 1 for intentionally triggered rollback, and 1 for the redelivery."
argument_list|,
name|getCount
argument_list|()
operator|<=
literal|2
argument_list|)
expr_stmt|;
comment|// should be printed 2 times due to one re-delivery after one failure
name|log
operator|.
name|info
argument_list|(
literal|"Exchange["
operator|+
name|getCount
argument_list|()
operator|+
literal|"]["
operator|+
operator|(
operator|(
name|getCount
argument_list|()
operator|<=
literal|1
operator|)
condition|?
literal|"Should rollback"
else|:
literal|"Should succeed"
operator|)
operator|+
literal|"] = "
operator|+
name|exchange
argument_list|)
expr_stmt|;
comment|// force rollback on the second attempt
if|if
condition|(
name|getCount
argument_list|()
operator|<=
literal|1
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Rollback should be intentionally triggered: count["
operator|+
name|getCount
argument_list|()
operator|+
literal|"]."
argument_list|)
throw|;
block|}
block|}
DECL|method|setCount (int count)
specifier|private
name|void
name|setCount
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|this
operator|.
name|count
operator|=
name|count
expr_stmt|;
block|}
DECL|method|getCount ()
specifier|public
name|int
name|getCount
parameter_list|()
block|{
return|return
name|count
return|;
block|}
block|}
end_class

end_unit

