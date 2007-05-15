begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.bam
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|bam
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
name|util
operator|.
name|Time
import|;
end_import

begin_comment
comment|/**  * A temporal rule  *  * @version $Revision: $  */
end_comment

begin_class
DECL|class|TemporalRule
specifier|public
class|class
name|TemporalRule
block|{
DECL|field|first
specifier|private
name|TimeExpression
name|first
decl_stmt|;
DECL|field|second
specifier|private
name|TimeExpression
name|second
decl_stmt|;
DECL|field|gap
specifier|private
name|Time
name|gap
decl_stmt|;
DECL|method|TemporalRule (TimeExpression left, TimeExpression right)
specifier|public
name|TemporalRule
parameter_list|(
name|TimeExpression
name|left
parameter_list|,
name|TimeExpression
name|right
parameter_list|)
block|{
name|this
operator|.
name|first
operator|=
name|left
expr_stmt|;
name|this
operator|.
name|second
operator|=
name|right
expr_stmt|;
block|}
comment|/*     public void process(Exchange exchange) {         Time firstTime = evaluateTime(exchange);         if (firstTime == null) {             // TODO add test that if second happes first             return;         }         Time secondTime = evaluateTime(exchange);         if (secondTime == null) {             // TODO add test that things have expired          }         else {             if (secondTime.delta(firstTime.plus(gap))> 0) {                 // TODO                            }         }     }     */
DECL|method|expectWithin (Time time)
specifier|public
name|TemporalRule
name|expectWithin
parameter_list|(
name|Time
name|time
parameter_list|)
block|{
return|return
name|this
return|;
block|}
DECL|method|errorIfOver (Time time)
specifier|public
name|TemporalRule
name|errorIfOver
parameter_list|(
name|Time
name|time
parameter_list|)
block|{
comment|// TODO
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

