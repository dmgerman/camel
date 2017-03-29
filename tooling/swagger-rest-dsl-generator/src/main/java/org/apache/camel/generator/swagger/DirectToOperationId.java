begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.generator.swagger
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|generator
operator|.
name|swagger
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|Operation
import|;
end_import

begin_class
DECL|class|DirectToOperationId
specifier|public
specifier|final
class|class
name|DirectToOperationId
implements|implements
name|DestinationGenerator
block|{
DECL|field|directRouteCount
specifier|private
specifier|final
name|AtomicInteger
name|directRouteCount
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|generateDestinationFor (final Operation operation)
specifier|public
name|String
name|generateDestinationFor
parameter_list|(
specifier|final
name|Operation
name|operation
parameter_list|)
block|{
return|return
literal|"direct:"
operator|+
name|Optional
operator|.
name|ofNullable
argument_list|(
name|operation
operator|.
name|getOperationId
argument_list|()
argument_list|)
operator|.
name|orElseGet
argument_list|(
name|this
operator|::
name|generateDirectName
argument_list|)
return|;
block|}
DECL|method|generateDirectName ()
name|String
name|generateDirectName
parameter_list|()
block|{
return|return
literal|"rest"
operator|+
name|directRouteCount
operator|.
name|incrementAndGet
argument_list|()
return|;
block|}
block|}
end_class

end_unit

