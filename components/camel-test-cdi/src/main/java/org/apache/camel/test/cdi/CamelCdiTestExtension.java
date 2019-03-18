begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|cdi
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
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Priority
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|event
operator|.
name|Observes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Alternative
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|AnnotatedMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|AnnotatedType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|Extension
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|ProcessAnnotatedType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|WithAnnotations
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|interceptor
operator|.
name|Interceptor
operator|.
name|Priority
operator|.
name|APPLICATION
import|;
end_import

begin_class
DECL|class|CamelCdiTestExtension
specifier|final
class|class
name|CamelCdiTestExtension
implements|implements
name|Extension
block|{
DECL|field|beans
specifier|private
specifier|final
name|Beans
name|beans
decl_stmt|;
DECL|method|CamelCdiTestExtension (Beans beans)
name|CamelCdiTestExtension
parameter_list|(
name|Beans
name|beans
parameter_list|)
block|{
name|this
operator|.
name|beans
operator|=
name|beans
expr_stmt|;
block|}
comment|/**      * Activates the alternatives declared with {@code @Beans} globally for the      * application.      *<p/>      * For every types and every methods of every types declared with      * {@link Beans#alternatives()}, the {@code Priority} annotation is added      * so that the corresponding alternatives are selected globally for the      * entire application.      *      * @see Beans      */
DECL|method|alternatives (@bserves @ithAnnotationsAlternative.class) ProcessAnnotatedType<T> pat)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|alternatives
parameter_list|(
annotation|@
name|Observes
annotation|@
name|WithAnnotations
argument_list|(
name|Alternative
operator|.
name|class
argument_list|)
name|ProcessAnnotatedType
argument_list|<
name|T
argument_list|>
name|pat
parameter_list|)
block|{
name|AnnotatedType
argument_list|<
name|T
argument_list|>
name|type
init|=
name|pat
operator|.
name|getAnnotatedType
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Arrays
operator|.
name|asList
argument_list|(
name|beans
operator|.
name|alternatives
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|type
operator|.
name|getJavaClass
argument_list|()
argument_list|)
condition|)
block|{
comment|// Only select globally the alternatives that are declared with @Beans
return|return;
block|}
name|Set
argument_list|<
name|AnnotatedMethod
argument_list|<
name|?
super|super
name|T
argument_list|>
argument_list|>
name|methods
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|AnnotatedMethod
argument_list|<
name|?
super|super
name|T
argument_list|>
name|method
range|:
name|type
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|method
operator|.
name|isAnnotationPresent
argument_list|(
name|Alternative
operator|.
name|class
argument_list|)
operator|&&
operator|!
name|method
operator|.
name|isAnnotationPresent
argument_list|(
name|Priority
operator|.
name|class
argument_list|)
condition|)
block|{
name|methods
operator|.
name|add
argument_list|(
operator|new
name|AnnotatedMethodDecorator
argument_list|<>
argument_list|(
name|method
argument_list|,
name|PriorityLiteral
operator|.
name|of
argument_list|(
name|APPLICATION
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|type
operator|.
name|isAnnotationPresent
argument_list|(
name|Alternative
operator|.
name|class
argument_list|)
operator|&&
operator|!
name|type
operator|.
name|isAnnotationPresent
argument_list|(
name|Priority
operator|.
name|class
argument_list|)
condition|)
block|{
name|pat
operator|.
name|setAnnotatedType
argument_list|(
operator|new
name|AnnotatedTypeDecorator
argument_list|<>
argument_list|(
name|type
argument_list|,
name|PriorityLiteral
operator|.
name|of
argument_list|(
name|APPLICATION
argument_list|)
argument_list|,
name|methods
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|methods
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|pat
operator|.
name|setAnnotatedType
argument_list|(
operator|new
name|AnnotatedTypeDecorator
argument_list|<>
argument_list|(
name|type
argument_list|,
name|methods
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

