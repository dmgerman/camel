begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|builder
operator|.
name|ExpressionClause
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
name|model
operator|.
name|language
operator|.
name|ExpressionType
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
name|Delayer
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
name|spi
operator|.
name|RouteContext
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;delay/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"delay"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|DelayerType
specifier|public
class|class
name|DelayerType
extends|extends
name|ExpressionNode
block|{
annotation|@
name|XmlAttribute
DECL|field|delayTime
specifier|private
name|Long
name|delayTime
init|=
literal|0L
decl_stmt|;
DECL|method|DelayerType ()
specifier|public
name|DelayerType
parameter_list|()
block|{     }
DECL|method|DelayerType (Expression processAtExpression)
specifier|public
name|DelayerType
parameter_list|(
name|Expression
name|processAtExpression
parameter_list|)
block|{
name|super
argument_list|(
name|processAtExpression
argument_list|)
expr_stmt|;
block|}
DECL|method|DelayerType (ExpressionType processAtExpression)
specifier|public
name|DelayerType
parameter_list|(
name|ExpressionType
name|processAtExpression
parameter_list|)
block|{
name|super
argument_list|(
name|processAtExpression
argument_list|)
expr_stmt|;
block|}
DECL|method|DelayerType (Expression processAtExpression, long delayTime)
specifier|public
name|DelayerType
parameter_list|(
name|Expression
name|processAtExpression
parameter_list|,
name|long
name|delayTime
parameter_list|)
block|{
name|super
argument_list|(
name|processAtExpression
argument_list|)
expr_stmt|;
name|this
operator|.
name|delayTime
operator|=
name|delayTime
expr_stmt|;
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
literal|"Delayer[on: "
operator|+
name|getExpression
argument_list|()
operator|+
literal|" delay: "
operator|+
name|delayTime
operator|+
literal|" -> "
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the delay time in millis to delay      * @param delay delay time in millis      * @return the builder      */
DECL|method|delayTime (Long delay)
specifier|public
name|DelayerType
name|delayTime
parameter_list|(
name|Long
name|delay
parameter_list|)
block|{
name|setDelayTime
argument_list|(
name|delay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the expression that the delayer will use      * @return the builder      */
DECL|method|expression ()
specifier|public
name|ExpressionClause
argument_list|<
name|DelayerType
argument_list|>
name|expression
parameter_list|()
block|{
return|return
name|ExpressionClause
operator|.
name|createAndSetExpression
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"delayer"
return|;
block|}
DECL|method|getDelayTime ()
specifier|public
name|Long
name|getDelayTime
parameter_list|()
block|{
return|return
name|delayTime
return|;
block|}
DECL|method|setDelayTime (Long delayTime)
specifier|public
name|void
name|setDelayTime
parameter_list|(
name|Long
name|delayTime
parameter_list|)
block|{
name|this
operator|.
name|delayTime
operator|=
name|delayTime
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Processor
name|childProcessor
init|=
name|routeContext
operator|.
name|createProcessor
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|Expression
name|processAtExpression
init|=
name|createAbsoluteTimeDelayExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
return|return
operator|new
name|Delayer
argument_list|(
name|childProcessor
argument_list|,
name|processAtExpression
argument_list|,
name|delayTime
argument_list|)
return|;
block|}
DECL|method|createAbsoluteTimeDelayExpression (RouteContext routeContext)
specifier|private
name|Expression
name|createAbsoluteTimeDelayExpression
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|ExpressionType
name|expr
init|=
name|getExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|expr
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|expr
operator|.
name|getExpression
argument_list|()
argument_list|)
operator|||
name|expr
operator|.
name|getExpressionValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|expr
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

