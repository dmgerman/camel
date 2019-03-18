begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.resequencer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|resequencer
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

begin_comment
comment|/**  * Compares elements of an {@link Exchange} sequence by comparing  *<code>long</code> values returned by this comparator's  *<code>expression</code>.  */
end_comment

begin_class
DECL|class|DefaultExchangeComparator
specifier|public
class|class
name|DefaultExchangeComparator
implements|implements
name|ExpressionResultComparator
block|{
DECL|field|expression
specifier|private
name|Expression
name|expression
decl_stmt|;
annotation|@
name|Override
DECL|method|setExpression (Expression expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|predecessor (Exchange o1, Exchange o2)
specifier|public
name|boolean
name|predecessor
parameter_list|(
name|Exchange
name|o1
parameter_list|,
name|Exchange
name|o2
parameter_list|)
block|{
name|long
name|n1
init|=
name|getSequenceNumber
argument_list|(
name|o1
argument_list|)
decl_stmt|;
name|long
name|n2
init|=
name|getSequenceNumber
argument_list|(
name|o2
argument_list|)
decl_stmt|;
return|return
name|n1
operator|==
operator|(
name|n2
operator|-
literal|1L
operator|)
return|;
block|}
annotation|@
name|Override
DECL|method|successor (Exchange o1, Exchange o2)
specifier|public
name|boolean
name|successor
parameter_list|(
name|Exchange
name|o1
parameter_list|,
name|Exchange
name|o2
parameter_list|)
block|{
name|long
name|n1
init|=
name|getSequenceNumber
argument_list|(
name|o1
argument_list|)
decl_stmt|;
name|long
name|n2
init|=
name|getSequenceNumber
argument_list|(
name|o2
argument_list|)
decl_stmt|;
return|return
name|n2
operator|==
operator|(
name|n1
operator|-
literal|1L
operator|)
return|;
block|}
annotation|@
name|Override
DECL|method|compare (Exchange o1, Exchange o2)
specifier|public
name|int
name|compare
parameter_list|(
name|Exchange
name|o1
parameter_list|,
name|Exchange
name|o2
parameter_list|)
block|{
name|Long
name|n1
init|=
name|getSequenceNumber
argument_list|(
name|o1
argument_list|)
decl_stmt|;
name|Long
name|n2
init|=
name|getSequenceNumber
argument_list|(
name|o2
argument_list|)
decl_stmt|;
return|return
name|n1
operator|.
name|compareTo
argument_list|(
name|n2
argument_list|)
return|;
block|}
DECL|method|getSequenceNumber (Exchange exchange)
specifier|private
name|Long
name|getSequenceNumber
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Long
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isValid (Exchange exchange)
specifier|public
name|boolean
name|isValid
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Long
name|num
init|=
literal|null
decl_stmt|;
try|try
block|{
name|num
operator|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|num
operator|!=
literal|null
return|;
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
literal|"Comparator["
operator|+
name|expression
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

