begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.flink
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flink
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|flink
operator|.
name|api
operator|.
name|java
operator|.
name|ExecutionEnvironment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|flink
operator|.
name|streaming
operator|.
name|api
operator|.
name|environment
operator|.
name|StreamExecutionEnvironment
import|;
end_import

begin_class
DECL|class|Flinks
specifier|public
specifier|final
class|class
name|Flinks
block|{
DECL|method|Flinks ()
specifier|private
name|Flinks
parameter_list|()
block|{      }
DECL|method|createExecutionEnvironment ()
specifier|public
specifier|static
name|ExecutionEnvironment
name|createExecutionEnvironment
parameter_list|()
block|{
return|return
name|ExecutionEnvironment
operator|.
name|getExecutionEnvironment
argument_list|()
return|;
block|}
DECL|method|createStreamExecutionEnvironment ()
specifier|public
specifier|static
name|StreamExecutionEnvironment
name|createStreamExecutionEnvironment
parameter_list|()
block|{
return|return
name|StreamExecutionEnvironment
operator|.
name|getExecutionEnvironment
argument_list|()
return|;
block|}
block|}
end_class

end_unit

