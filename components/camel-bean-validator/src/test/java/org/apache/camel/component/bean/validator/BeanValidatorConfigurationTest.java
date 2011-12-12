begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean.validator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
operator|.
name|validator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ConstraintValidator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|ConstraintValidatorFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|MessageInterpolator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|Path
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|validation
operator|.
name|TraversableResolver
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
name|impl
operator|.
name|JndiRegistry
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
name|impl
operator|.
name|ProcessorEndpoint
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hibernate
operator|.
name|validator
operator|.
name|engine
operator|.
name|ConstraintValidatorFactoryImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hibernate
operator|.
name|validator
operator|.
name|engine
operator|.
name|ValidatorImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hibernate
operator|.
name|validator
operator|.
name|engine
operator|.
name|resolver
operator|.
name|DefaultTraversableResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hibernate
operator|.
name|validator
operator|.
name|messageinterpolation
operator|.
name|ResourceBundleMessageInterpolator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|BeanValidatorConfigurationTest
specifier|public
class|class
name|BeanValidatorConfigurationTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|messageInterpolator
specifier|private
name|MessageInterpolator
name|messageInterpolator
decl_stmt|;
DECL|field|traversableResolver
specifier|private
name|TraversableResolver
name|traversableResolver
decl_stmt|;
DECL|field|constraintValidatorFactory
specifier|private
name|ConstraintValidatorFactory
name|constraintValidatorFactory
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|messageInterpolator
operator|=
operator|new
name|MyMessageInterpolator
argument_list|()
expr_stmt|;
name|this
operator|.
name|traversableResolver
operator|=
operator|new
name|MyTraversableResolver
argument_list|()
expr_stmt|;
name|this
operator|.
name|constraintValidatorFactory
operator|=
operator|new
name|MyConstraintValidatorFactory
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"myMessageInterpolator"
argument_list|,
name|this
operator|.
name|messageInterpolator
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"myTraversableResolver"
argument_list|,
name|this
operator|.
name|traversableResolver
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"myConstraintValidatorFactory"
argument_list|,
name|this
operator|.
name|constraintValidatorFactory
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Test
DECL|method|configureWithDefaults ()
specifier|public
name|void
name|configureWithDefaults
parameter_list|()
throws|throws
name|Exception
block|{
name|ProcessorEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"bean-validator://x"
argument_list|,
name|ProcessorEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|BeanValidator
name|processor
init|=
operator|(
name|BeanValidator
operator|)
name|endpoint
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|processor
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|processor
operator|.
name|getValidator
argument_list|()
operator|instanceof
name|ValidatorImpl
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|processor
operator|.
name|getMessageInterpolator
argument_list|()
operator|instanceof
name|ResourceBundleMessageInterpolator
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|processor
operator|.
name|getTraversableResolver
argument_list|()
operator|instanceof
name|DefaultTraversableResolver
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|processor
operator|.
name|getConstraintValidatorFactory
argument_list|()
operator|instanceof
name|ConstraintValidatorFactoryImpl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|configureBeanValidator ()
specifier|public
name|void
name|configureBeanValidator
parameter_list|()
throws|throws
name|Exception
block|{
name|ProcessorEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"bean-validator://x"
operator|+
literal|"?group=org.apache.camel.component.bean.validator.OptionalChecks"
operator|+
literal|"&messageInterpolator=#myMessageInterpolator"
operator|+
literal|"&traversableResolver=#myTraversableResolver"
operator|+
literal|"&constraintValidatorFactory=myConstraintValidatorFactory"
argument_list|,
name|ProcessorEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|BeanValidator
name|processor
init|=
operator|(
name|BeanValidator
operator|)
name|endpoint
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.camel.component.bean.validator.OptionalChecks"
argument_list|,
name|processor
operator|.
name|getGroup
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|processor
operator|.
name|getValidator
argument_list|()
operator|instanceof
name|ValidatorImpl
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|processor
operator|.
name|getMessageInterpolator
argument_list|()
argument_list|,
name|this
operator|.
name|messageInterpolator
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|processor
operator|.
name|getTraversableResolver
argument_list|()
argument_list|,
name|this
operator|.
name|traversableResolver
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|processor
operator|.
name|getConstraintValidatorFactory
argument_list|()
argument_list|,
name|this
operator|.
name|constraintValidatorFactory
argument_list|)
expr_stmt|;
block|}
DECL|class|MyMessageInterpolator
class|class
name|MyMessageInterpolator
implements|implements
name|MessageInterpolator
block|{
DECL|method|interpolate (String messageTemplate, Context context)
specifier|public
name|String
name|interpolate
parameter_list|(
name|String
name|messageTemplate
parameter_list|,
name|Context
name|context
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
DECL|method|interpolate (String messageTemplate, Context context, Locale locale)
specifier|public
name|String
name|interpolate
parameter_list|(
name|String
name|messageTemplate
parameter_list|,
name|Context
name|context
parameter_list|,
name|Locale
name|locale
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|class|MyTraversableResolver
class|class
name|MyTraversableResolver
implements|implements
name|TraversableResolver
block|{
DECL|method|isCascadable (Object traversableObject, Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType)
specifier|public
name|boolean
name|isCascadable
parameter_list|(
name|Object
name|traversableObject
parameter_list|,
name|Node
name|traversableProperty
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|rootBeanType
parameter_list|,
name|Path
name|pathToTraversableObject
parameter_list|,
name|ElementType
name|elementType
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
DECL|method|isReachable (Object traversableObject, Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType)
specifier|public
name|boolean
name|isReachable
parameter_list|(
name|Object
name|traversableObject
parameter_list|,
name|Node
name|traversableProperty
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|rootBeanType
parameter_list|,
name|Path
name|pathToTraversableObject
parameter_list|,
name|ElementType
name|elementType
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|class|MyConstraintValidatorFactory
class|class
name|MyConstraintValidatorFactory
implements|implements
name|ConstraintValidatorFactory
block|{
DECL|method|getInstance (Class<T> key)
specifier|public
parameter_list|<
name|T
extends|extends
name|ConstraintValidator
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
parameter_list|>
name|T
name|getInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|key
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

