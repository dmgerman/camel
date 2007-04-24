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

begin_comment
comment|/**  * A builder of a number of different {@link Processor} implementations  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|ProcessorBuilder
specifier|public
class|class
name|ProcessorBuilder
block|{
comment|/**      * Creates a processor which sets the body of the IN message to the value of the expression      */
DECL|method|setBody (final Expression<E> expression)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Processor
argument_list|<
name|E
argument_list|>
name|setBody
parameter_list|(
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|Object
name|newBody
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|newBody
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"setBody("
operator|+
name|expression
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Creates a processor which sets the body of the IN message to the value of the expression      */
DECL|method|setOutBody (final Expression<E> expression)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Processor
argument_list|<
name|E
argument_list|>
name|setOutBody
parameter_list|(
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|Object
name|newBody
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|newBody
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"setOutBody("
operator|+
name|expression
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Sets the header on the IN message      */
DECL|method|setHeader (final String name, final Expression<E> expression)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Processor
argument_list|<
name|E
argument_list|>
name|setHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|Object
name|value
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"setHeader("
operator|+
name|name
operator|+
literal|", "
operator|+
name|expression
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Sets the header on the OUT message      */
DECL|method|setOutHeader (final String name, final Expression<E> expression)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Processor
argument_list|<
name|E
argument_list|>
name|setOutHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|Object
name|value
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"setOutHeader("
operator|+
name|name
operator|+
literal|", "
operator|+
name|expression
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Sets the property on the exchange      */
DECL|method|setProperty (final String name, final Expression<E> expression)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Processor
argument_list|<
name|E
argument_list|>
name|setProperty
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Expression
argument_list|<
name|E
argument_list|>
name|expression
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
name|Object
name|value
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"setProperty("
operator|+
name|name
operator|+
literal|", "
operator|+
name|expression
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

