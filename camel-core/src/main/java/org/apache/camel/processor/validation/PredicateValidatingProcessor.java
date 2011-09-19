begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.validation
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|validation
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
name|Predicate
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
name|Traceable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A processor which validates the content of the inbound message body  * against a predicate.  *   * @version   */
end_comment

begin_class
DECL|class|PredicateValidatingProcessor
specifier|public
class|class
name|PredicateValidatingProcessor
implements|implements
name|Processor
implements|,
name|Traceable
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PredicateValidatingProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|predicate
specifier|private
specifier|final
name|Predicate
name|predicate
decl_stmt|;
DECL|method|PredicateValidatingProcessor (Predicate predicate)
specifier|public
name|PredicateValidatingProcessor
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|this
operator|.
name|predicate
operator|=
name|predicate
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
name|boolean
name|matches
init|=
name|predicate
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
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
literal|"Validation {} for {} with Predicate[{}]"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|matches
condition|?
literal|"succeed"
else|:
literal|"failed"
block|,
name|exchange
block|,
name|predicate
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|matches
condition|)
block|{
throw|throw
operator|new
name|PredicateValidationException
argument_list|(
name|exchange
argument_list|,
name|predicate
argument_list|)
throw|;
block|}
block|}
DECL|method|getPredicate ()
specifier|public
name|Predicate
name|getPredicate
parameter_list|()
block|{
return|return
name|predicate
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
literal|"validate("
operator|+
name|predicate
operator|+
literal|")"
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"validate["
operator|+
name|predicate
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

