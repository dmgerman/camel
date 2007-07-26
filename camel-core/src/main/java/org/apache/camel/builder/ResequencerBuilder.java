begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|Service
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
name|Resequencer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|ResequencerBuilder
specifier|public
class|class
name|ResequencerBuilder
extends|extends
name|FromBuilder
block|{
DECL|field|expressions
specifier|private
specifier|final
name|List
argument_list|<
name|Expression
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|expressions
decl_stmt|;
DECL|field|batchTimeout
specifier|private
name|long
name|batchTimeout
init|=
literal|1000L
decl_stmt|;
DECL|field|batchSize
specifier|private
name|int
name|batchSize
init|=
literal|100
decl_stmt|;
DECL|method|ResequencerBuilder (FromBuilder builder, List<Expression<Exchange>> expressions)
specifier|public
name|ResequencerBuilder
parameter_list|(
name|FromBuilder
name|builder
parameter_list|,
name|List
argument_list|<
name|Expression
argument_list|<
name|Exchange
argument_list|>
argument_list|>
name|expressions
parameter_list|)
block|{
name|super
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|this
operator|.
name|expressions
operator|=
name|expressions
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRoute ()
specifier|public
name|Route
name|createRoute
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Processor
name|processor
init|=
name|super
operator|.
name|createProcessor
argument_list|()
decl_stmt|;
specifier|final
name|Resequencer
name|resequencer
init|=
operator|new
name|Resequencer
argument_list|(
name|getFrom
argument_list|()
argument_list|,
name|processor
argument_list|,
name|expressions
argument_list|)
decl_stmt|;
return|return
operator|new
name|Route
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|getFrom
argument_list|()
argument_list|,
name|resequencer
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ResequencerRoute["
operator|+
name|getEndpoint
argument_list|()
operator|+
literal|" -> "
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
block|}
return|;
block|}
comment|// Builder methods
comment|//-------------------------------------------------------------------------
DECL|method|batchSize (int batchSize)
specifier|public
name|ResequencerBuilder
name|batchSize
parameter_list|(
name|int
name|batchSize
parameter_list|)
block|{
name|setBatchSize
argument_list|(
name|batchSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|batchTimeout (int batchTimeout)
specifier|public
name|ResequencerBuilder
name|batchTimeout
parameter_list|(
name|int
name|batchTimeout
parameter_list|)
block|{
name|setBatchTimeout
argument_list|(
name|batchTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getBatchSize ()
specifier|public
name|int
name|getBatchSize
parameter_list|()
block|{
return|return
name|batchSize
return|;
block|}
DECL|method|setBatchSize (int batchSize)
specifier|public
name|void
name|setBatchSize
parameter_list|(
name|int
name|batchSize
parameter_list|)
block|{
name|this
operator|.
name|batchSize
operator|=
name|batchSize
expr_stmt|;
block|}
DECL|method|getBatchTimeout ()
specifier|public
name|long
name|getBatchTimeout
parameter_list|()
block|{
return|return
name|batchTimeout
return|;
block|}
DECL|method|setBatchTimeout (long batchTimeout)
specifier|public
name|void
name|setBatchTimeout
parameter_list|(
name|long
name|batchTimeout
parameter_list|)
block|{
name|this
operator|.
name|batchTimeout
operator|=
name|batchTimeout
expr_stmt|;
block|}
block|}
end_class

end_unit

