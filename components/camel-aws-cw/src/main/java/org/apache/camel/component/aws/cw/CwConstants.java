begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.cw
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
name|cw
package|;
end_package

begin_comment
comment|/**  * Constants used in Camel AWS CW module  */
end_comment

begin_interface
DECL|interface|CwConstants
specifier|public
interface|interface
name|CwConstants
block|{
DECL|field|METRIC_NAMESPACE
name|String
name|METRIC_NAMESPACE
init|=
literal|"CamelAwsCwMetricNamespace"
decl_stmt|;
DECL|field|METRIC_NAME
name|String
name|METRIC_NAME
init|=
literal|"CamelAwsCwMetricName"
decl_stmt|;
DECL|field|METRIC_VALUE
name|String
name|METRIC_VALUE
init|=
literal|"CamelAwsCwMetricValue"
decl_stmt|;
DECL|field|METRIC_UNIT
name|String
name|METRIC_UNIT
init|=
literal|"CamelAwsCwMetricUnit"
decl_stmt|;
DECL|field|METRIC_TIMESTAMP
name|String
name|METRIC_TIMESTAMP
init|=
literal|"CamelAwsCwMetricTimestamp"
decl_stmt|;
DECL|field|METRIC_DIMENSIONS
name|String
name|METRIC_DIMENSIONS
init|=
literal|"CamelAwsCwMetricDimensions"
decl_stmt|;
DECL|field|METRIC_DIMENSION_NAME
name|String
name|METRIC_DIMENSION_NAME
init|=
literal|"CamelAwsCwMetricDimensionName"
decl_stmt|;
DECL|field|METRIC_DIMENSION_VALUE
name|String
name|METRIC_DIMENSION_VALUE
init|=
literal|"CamelAwsCwMetricDimensionValue"
decl_stmt|;
block|}
end_interface

end_unit

