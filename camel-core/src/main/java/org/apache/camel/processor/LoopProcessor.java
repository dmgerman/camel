begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|Expression
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
name|util
operator|.
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * The processor which sends messages in a loop.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|LoopProcessor
specifier|public
class|class
name|LoopProcessor
extends|extends
name|DelegateProcessor
implements|implements
name|Traceable
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|LoopProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|expression
specifier|private
specifier|final
name|Expression
name|expression
decl_stmt|;
comment|// TODO: should support async routing engine
DECL|method|LoopProcessor (Expression expression, Processor processor)
specifier|public
name|LoopProcessor
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
annotation|@
name|Override
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
comment|// Intermediate conversion to String is needed when direct conversion to Integer is not available
comment|// but evaluation result is a textual representation of a numeric value.
name|String
name|text
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|count
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|text
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|LOOP_SIZE
argument_list|,
name|count
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"LoopProcessor: iteration #"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|LOOP_INDEX
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Loop[for: "
operator|+
name|expression
operator|+
literal|" times do: "
operator|+
name|getProcessor
argument_list|()
operator|+
literal|"]"
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"loop["
operator|+
name|expression
operator|+
literal|"]"
return|;
block|}
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
block|}
end_class

end_unit

