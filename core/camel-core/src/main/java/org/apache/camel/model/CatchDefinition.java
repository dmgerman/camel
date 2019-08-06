begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|List
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
name|XmlElement
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
name|XmlElementRef
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|spi
operator|.
name|AsPredicate
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Catches exceptions as part of a try, catch, finally block  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"error"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"doCatch"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CatchDefinition
specifier|public
class|class
name|CatchDefinition
extends|extends
name|ProcessorDefinition
argument_list|<
name|CatchDefinition
argument_list|>
implements|implements
name|OutputNode
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"exception"
argument_list|)
DECL|field|exceptions
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|exceptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"onWhen"
argument_list|)
annotation|@
name|AsPredicate
DECL|field|onWhen
specifier|private
name|WhenDefinition
name|onWhen
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|outputs
specifier|private
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|exceptionClasses
specifier|private
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|exceptionClasses
decl_stmt|;
DECL|method|CatchDefinition ()
specifier|public
name|CatchDefinition
parameter_list|()
block|{     }
DECL|method|CatchDefinition (List<Class<? extends Throwable>> exceptionClasses)
specifier|public
name|CatchDefinition
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|exceptionClasses
parameter_list|)
block|{
name|this
operator|.
name|exceptionClasses
operator|=
name|exceptionClasses
expr_stmt|;
block|}
DECL|method|CatchDefinition (Class<? extends Throwable> exceptionType)
specifier|public
name|CatchDefinition
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|exceptionType
parameter_list|)
block|{
name|exceptionClasses
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|exceptionClasses
operator|.
name|add
argument_list|(
name|exceptionType
argument_list|)
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
literal|"DoCatch[ "
operator|+
name|getExceptionClasses
argument_list|()
operator|+
literal|" -> "
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
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
literal|"doCatch"
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"doCatch[ "
operator|+
name|getExceptionClasses
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
return|;
block|}
DECL|method|setOutputs (List<ProcessorDefinition<?>> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
parameter_list|)
block|{
name|this
operator|.
name|outputs
operator|=
name|outputs
expr_stmt|;
block|}
DECL|method|getExceptionClasses ()
specifier|public
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|getExceptionClasses
parameter_list|()
block|{
return|return
name|exceptionClasses
return|;
block|}
DECL|method|setExceptionClasses (List<Class<? extends Throwable>> exceptionClasses)
specifier|public
name|void
name|setExceptionClasses
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|exceptionClasses
parameter_list|)
block|{
name|this
operator|.
name|exceptionClasses
operator|=
name|exceptionClasses
expr_stmt|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
comment|/**      * The exceptions to catch.      *      * @param exceptionClasses  a list of the exception classes      * @return the builder      * @deprecated use {@link #exception(Class[])}      */
annotation|@
name|Deprecated
DECL|method|exceptionClasses (List<Class<? extends Throwable>> exceptionClasses)
specifier|public
name|CatchDefinition
name|exceptionClasses
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|exceptionClasses
parameter_list|)
block|{
name|setExceptionClasses
argument_list|(
name|exceptionClasses
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * The exception(s) to catch.      *      * @param exceptions  one or more exceptions      * @return the builder      */
DECL|method|exception (Class<? extends Throwable>.... exceptions)
specifier|public
name|CatchDefinition
name|exception
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
modifier|...
name|exceptions
parameter_list|)
block|{
if|if
condition|(
name|exceptionClasses
operator|==
literal|null
condition|)
block|{
name|exceptionClasses
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|exceptions
operator|!=
literal|null
condition|)
block|{
name|exceptionClasses
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|exceptions
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Sets an additional predicate that should be true before the onCatch is triggered.      *<p/>      * To be used for fine grained controlling whether a thrown exception should be intercepted      * by this exception type or not.      *      * @param predicate  predicate that determines true or false      * @return the builder      */
DECL|method|onWhen (@sPredicate Predicate predicate)
specifier|public
name|CatchDefinition
name|onWhen
parameter_list|(
annotation|@
name|AsPredicate
name|Predicate
name|predicate
parameter_list|)
block|{
name|setOnWhen
argument_list|(
operator|new
name|WhenDefinition
argument_list|(
name|predicate
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the exception class that the CatchType want to catch      *      * @param exception  the exception of class      * @return the builder      * @deprecated use {@link #exception(Class[])}      */
annotation|@
name|Deprecated
DECL|method|exceptionClasses (Class<? extends Throwable> exception)
specifier|public
name|CatchDefinition
name|exceptionClasses
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|exception
parameter_list|)
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|list
init|=
name|getExceptionClasses
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|exception
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getExceptions ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExceptions
parameter_list|()
block|{
return|return
name|exceptions
return|;
block|}
DECL|method|setExceptions (List<String> exceptions)
specifier|public
name|void
name|setExceptions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|exceptions
parameter_list|)
block|{
name|this
operator|.
name|exceptions
operator|=
name|exceptions
expr_stmt|;
block|}
DECL|method|getOnWhen ()
specifier|public
name|WhenDefinition
name|getOnWhen
parameter_list|()
block|{
return|return
name|onWhen
return|;
block|}
DECL|method|setOnWhen (WhenDefinition onWhen)
specifier|public
name|void
name|setOnWhen
parameter_list|(
name|WhenDefinition
name|onWhen
parameter_list|)
block|{
name|this
operator|.
name|onWhen
operator|=
name|onWhen
expr_stmt|;
block|}
block|}
end_class

end_unit

