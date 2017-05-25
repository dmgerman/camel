begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.grpc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|grpc
package|;
end_package

begin_comment
comment|/*  * Available values for the request and response processing strategies  */
end_comment

begin_enum
DECL|enum|GrpcProcessingStrategies
specifier|public
enum|enum
name|GrpcProcessingStrategies
block|{
DECL|enumConstant|AGGREGATION
name|AGGREGATION
argument_list|(
literal|"AGGREGATION"
argument_list|)
block|,
DECL|enumConstant|PROPAGATION
name|PROPAGATION
argument_list|(
literal|"PROPAGATION"
argument_list|)
block|;
DECL|field|strategy
specifier|private
specifier|final
name|String
name|strategy
decl_stmt|;
DECL|method|GrpcProcessingStrategies (final String strategy)
name|GrpcProcessingStrategies
parameter_list|(
specifier|final
name|String
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|strategy
operator|=
name|strategy
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
name|strategy
return|;
block|}
block|}
end_enum

end_unit

