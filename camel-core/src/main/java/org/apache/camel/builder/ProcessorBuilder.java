begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Arrays
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
comment|/**  * A builder of a number of different {@link Processor} implementations  *  * @version   */
end_comment

begin_class
DECL|class|ProcessorBuilder
specifier|public
specifier|final
class|class
name|ProcessorBuilder
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ProcessorBuilder ()
specifier|private
name|ProcessorBuilder
parameter_list|()
block|{     }
comment|/**      * Creates a processor which sets the body of the IN message to the value of the expression      */
DECL|method|setBody (final Expression expression)
specifier|public
specifier|static
name|Processor
name|setBody
parameter_list|(
specifier|final
name|Expression
name|expression
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
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
argument_list|,
name|Object
operator|.
name|class
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
comment|/**      * Creates a processor which sets the body of the OUT message to the value of the expression      */
DECL|method|setOutBody (final Expression expression)
specifier|public
specifier|static
name|Processor
name|setOutBody
parameter_list|(
specifier|final
name|Expression
name|expression
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
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
argument_list|,
name|Object
operator|.
name|class
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
comment|/**      * Creates a processor which sets the body of the FAULT message to the value of the expression      */
DECL|method|setFaultBody (final Expression expression)
specifier|public
specifier|static
name|Processor
name|setFaultBody
parameter_list|(
specifier|final
name|Expression
name|expression
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
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
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setFault
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
literal|"setFaultBody("
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
DECL|method|setHeader (final String name, final Expression expression)
specifier|public
specifier|static
name|Processor
name|setHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Expression
name|expression
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
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
argument_list|,
name|Object
operator|.
name|class
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
DECL|method|setOutHeader (final String name, final Expression expression)
specifier|public
specifier|static
name|Processor
name|setOutHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Expression
name|expression
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
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
argument_list|,
name|Object
operator|.
name|class
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
comment|/**      * Sets the header on the FAULT message      */
DECL|method|setFaultHeader (final String name, final Expression expression)
specifier|public
specifier|static
name|Processor
name|setFaultHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Expression
name|expression
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
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
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setFault
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
literal|"setFaultHeader("
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
DECL|method|setProperty (final String name, final Expression expression)
specifier|public
specifier|static
name|Processor
name|setProperty
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|Expression
name|expression
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
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
argument_list|,
name|Object
operator|.
name|class
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
comment|/**      * Removes the header on the IN message      */
DECL|method|removeHeader (final String name)
specifier|public
specifier|static
name|Processor
name|removeHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|name
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
literal|"removeHeader("
operator|+
name|name
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Removes the headers on the IN message      */
DECL|method|removeHeaders (final String pattern)
specifier|public
specifier|static
name|Processor
name|removeHeaders
parameter_list|(
specifier|final
name|String
name|pattern
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeaders
argument_list|(
name|pattern
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
literal|"removeHeaders("
operator|+
name|pattern
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Removes all headers on the IN message, except for the ones provided in the<tt>names</tt> parameter      */
DECL|method|removeHeaders (final String pattern, final String... exceptionPatterns)
specifier|public
specifier|static
name|Processor
name|removeHeaders
parameter_list|(
specifier|final
name|String
name|pattern
parameter_list|,
specifier|final
name|String
modifier|...
name|exceptionPatterns
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeaders
argument_list|(
name|pattern
argument_list|,
name|exceptionPatterns
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
literal|"removeHeaders("
operator|+
name|pattern
operator|+
literal|", "
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|exceptionPatterns
argument_list|)
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Removes the header on the FAULT message      * @deprecated use {@link #removeHeader(String)}      */
annotation|@
name|Deprecated
DECL|method|removeFaultHeader (final String name)
specifier|public
specifier|static
name|Processor
name|removeFaultHeader
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setFault
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|name
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
literal|"removeFaultHeader("
operator|+
name|name
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Removes the property on the exchange      */
DECL|method|removeProperty (final String name)
specifier|public
specifier|static
name|Processor
name|removeProperty
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|removeProperty
argument_list|(
name|name
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
literal|"removeProperty("
operator|+
name|name
operator|+
literal|")"
return|;
block|}
block|}
return|;
block|}
comment|/**      * Throws an exception      */
DECL|method|throwException (final Exception ex)
specifier|public
specifier|static
name|Processor
name|throwException
parameter_list|(
specifier|final
name|Exception
name|ex
parameter_list|)
block|{
return|return
operator|new
name|Processor
argument_list|()
block|{
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
throw|throw
name|ex
throw|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"throwException("
operator|+
name|ex
operator|.
name|toString
argument_list|()
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

