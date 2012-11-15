begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|cloudwatch
operator|.
name|AmazonCloudWatchClient
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
name|aws
operator|.
name|sns
operator|.
name|SnsComponent
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|*
import|;
end_import

begin_class
DECL|class|CwComponentConfigurationTest
specifier|public
class|class
name|CwComponentConfigurationTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|NOW
specifier|private
specifier|static
specifier|final
name|Date
name|NOW
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
DECL|field|amazonCwClient
name|AmazonCloudWatchClient
name|amazonCwClient
init|=
name|mock
argument_list|(
name|AmazonCloudWatchClient
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|createEndpointWithAllOptions ()
specifier|public
name|void
name|createEndpointWithAllOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|CwComponent
name|component
init|=
operator|new
name|CwComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|CwEndpoint
name|endpoint
init|=
operator|(
name|CwEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-cw://camel.apache.org/test?amazonCwClient=#amazonCwClient&name=testMetric&value=2&unit=Count&timestamp=#now"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camel.apache.org/test"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testMetric"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Count"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getUnit
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|NOW
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|createEndpointWithoutAccessKeyConfiguration ()
specifier|public
name|void
name|createEndpointWithoutAccessKeyConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-cw://camel.apache.org/test?secretKey=yyy"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|createEndpointWithoutSecretKeyConfiguration ()
specifier|public
name|void
name|createEndpointWithoutSecretKeyConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|CwComponent
name|component
init|=
operator|new
name|CwComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-cw://camel.apache.org/test?accessKey=xxx"
argument_list|)
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
literal|"amazonCwClient"
argument_list|,
name|amazonCwClient
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"now"
argument_list|,
name|NOW
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
block|}
end_class

end_unit

