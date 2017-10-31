begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.xray
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|xray
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
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|invoke
operator|.
name|MethodHandles
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|xray
operator|.
name|AWSXRay
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|xray
operator|.
name|entities
operator|.
name|Subsegment
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
name|CamelContext
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
name|component
operator|.
name|bean
operator|.
name|BeanProcessor
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
name|BeanDefinition
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
name|ProcessDefinition
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
name|ProcessorDefinition
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
name|DelegateAsyncProcessor
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
name|DelegateSyncProcessor
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
name|InterceptStrategy
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

begin_class
DECL|class|TraceAnnotatedTracingStrategy
specifier|public
class|class
name|TraceAnnotatedTracingStrategy
implements|implements
name|InterceptStrategy
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
name|MethodHandles
operator|.
name|lookup
argument_list|()
operator|.
name|lookupClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|wrapProcessorInInterceptors (CamelContext camelContext, ProcessorDefinition<?> processorDefinition, Processor target, Processor nextTarget)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|processorDefinition
parameter_list|,
name|Processor
name|target
parameter_list|,
name|Processor
name|nextTarget
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|processorClass
init|=
name|processorDefinition
operator|.
name|getClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|processorDefinition
operator|instanceof
name|BeanDefinition
condition|)
block|{
name|BeanProcessor
name|beanProcessor
init|=
operator|(
name|BeanProcessor
operator|)
name|nextTarget
decl_stmt|;
name|processorClass
operator|=
name|beanProcessor
operator|.
name|getBean
argument_list|()
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processorDefinition
operator|instanceof
name|ProcessDefinition
condition|)
block|{
name|DelegateSyncProcessor
name|syncProcessor
init|=
operator|(
name|DelegateSyncProcessor
operator|)
name|nextTarget
decl_stmt|;
name|processorClass
operator|=
name|syncProcessor
operator|.
name|getProcessor
argument_list|()
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|processorClass
operator|.
name|isAnnotationPresent
argument_list|(
name|XRayTrace
operator|.
name|class
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"{} does not contain an @Trace annotation. Skipping interception"
argument_list|,
name|processorClass
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|DelegateAsyncProcessor
argument_list|(
name|target
argument_list|)
return|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Wrapping process definition {} of target {} in order for recording its trace"
argument_list|,
name|processorDefinition
argument_list|,
name|processorClass
argument_list|)
expr_stmt|;
name|Annotation
name|annotation
init|=
name|processorClass
operator|.
name|getAnnotation
argument_list|(
name|XRayTrace
operator|.
name|class
argument_list|)
decl_stmt|;
name|XRayTrace
name|trace
init|=
operator|(
name|XRayTrace
operator|)
name|annotation
decl_stmt|;
name|String
name|metricName
init|=
name|trace
operator|.
name|metricName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|metricName
argument_list|)
condition|)
block|{
name|metricName
operator|=
name|processorClass
operator|.
name|getSimpleName
argument_list|()
expr_stmt|;
block|}
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|processorClass
decl_stmt|;
specifier|final
name|String
name|name
init|=
name|metricName
decl_stmt|;
return|return
operator|new
name|DelegateAsyncProcessor
argument_list|(
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
lambda|->
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating new subsegment for {} of type {} - EIP {}"
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|target
argument_list|)
expr_stmt|;
name|Subsegment
name|subsegment
init|=
name|AWSXRay
operator|.
name|beginSubsegment
argument_list|(
name|name
argument_list|)
decl_stmt|;
try|try
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing EIP {}"
argument_list|,
name|target
argument_list|)
expr_stmt|;
name|target
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Handling exception thrown by invoked EIP {}"
argument_list|,
name|target
argument_list|)
expr_stmt|;
name|subsegment
operator|.
name|addException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
finally|finally
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Closing down subsegment for {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|subsegment
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

