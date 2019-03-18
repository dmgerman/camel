begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spark.annotations
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
operator|.
name|annotations
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
name|component
operator|.
name|spark
operator|.
name|RddCallback
import|;
end_import

begin_comment
comment|/**  * Provides facade for working with annotated RDD callbacks i.e. POJO classes with an appropriate annotations on  * selected methods.  */
end_comment

begin_class
DECL|class|AnnotatedRddCallback
specifier|public
specifier|final
class|class
name|AnnotatedRddCallback
block|{
DECL|method|AnnotatedRddCallback ()
specifier|private
name|AnnotatedRddCallback
parameter_list|()
block|{     }
DECL|method|annotatedRddCallback (Object objectWithCallback)
specifier|public
specifier|static
name|RddCallback
name|annotatedRddCallback
parameter_list|(
name|Object
name|objectWithCallback
parameter_list|)
block|{
return|return
operator|new
name|AnnotatedRddCallbackProxy
argument_list|(
name|objectWithCallback
argument_list|)
return|;
block|}
DECL|method|annotatedRddCallback (Object objectWithCallback, CamelContext camelContext)
specifier|public
specifier|static
name|RddCallback
name|annotatedRddCallback
parameter_list|(
name|Object
name|objectWithCallback
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
return|return
operator|new
name|AnnotatedRddCallbackProxy
argument_list|(
name|objectWithCallback
argument_list|,
name|camelContext
argument_list|)
return|;
block|}
block|}
end_class

end_unit

