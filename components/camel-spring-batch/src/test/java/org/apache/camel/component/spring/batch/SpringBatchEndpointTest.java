begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.batch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|batch
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
name|EndpointInject
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
name|FailedToCreateRouteException
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|DefaultCamelContext
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
name|SimpleRegistry
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
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|reflect
operator|.
name|FieldUtils
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
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|ArgumentCaptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|runners
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|Job
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|JobExecution
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|JobParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|batch
operator|.
name|core
operator|.
name|launch
operator|.
name|JobLauncher
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|BDDMockito
operator|.
name|*
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|SpringBatchEndpointTest
specifier|public
class|class
name|SpringBatchEndpointTest
extends|extends
name|CamelTestSupport
block|{
comment|// Fixtures
annotation|@
name|Mock
DECL|field|jobLauncher
name|JobLauncher
name|jobLauncher
decl_stmt|;
annotation|@
name|Mock
DECL|field|alternativeJobLauncher
name|JobLauncher
name|alternativeJobLauncher
decl_stmt|;
annotation|@
name|Mock
DECL|field|job
name|Job
name|job
decl_stmt|;
comment|// Camel fixtures
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:test"
argument_list|)
DECL|field|mockEndpoint
name|MockEndpoint
name|mockEndpoint
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"spring-batch:mockJob"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|public
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
literal|"jobLauncher"
argument_list|,
name|jobLauncher
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"alternativeJobLauncher"
argument_list|,
name|alternativeJobLauncher
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"mockJob"
argument_list|,
name|job
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
comment|// Tests
annotation|@
name|Test
DECL|method|shouldInjectJobToEndpoint ()
specifier|public
name|void
name|shouldInjectJobToEndpoint
parameter_list|()
throws|throws
name|IllegalAccessException
block|{
name|SpringBatchEndpoint
name|batchEndpoint
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"spring-batch:mockJob"
argument_list|,
name|SpringBatchEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Job
name|batchEndpointJob
init|=
operator|(
name|Job
operator|)
name|FieldUtils
operator|.
name|readField
argument_list|(
name|batchEndpoint
argument_list|,
literal|"job"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|job
argument_list|,
name|batchEndpointJob
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldRunJob ()
specifier|public
name|void
name|shouldRunJob
parameter_list|()
throws|throws
name|Exception
block|{
comment|// When
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Start the job, please."
argument_list|)
expr_stmt|;
comment|// Then
name|verify
argument_list|(
name|jobLauncher
argument_list|)
operator|.
name|run
argument_list|(
name|eq
argument_list|(
name|job
argument_list|)
argument_list|,
name|any
argument_list|(
name|JobParameters
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldReturnJobExecution ()
specifier|public
name|void
name|shouldReturnJobExecution
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|JobExecution
name|jobExecution
init|=
name|mock
argument_list|(
name|JobExecution
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|jobLauncher
operator|.
name|run
argument_list|(
name|eq
argument_list|(
name|job
argument_list|)
argument_list|,
name|any
argument_list|(
name|JobParameters
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|jobExecution
argument_list|)
expr_stmt|;
comment|// When
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Start the job, please."
argument_list|)
expr_stmt|;
comment|// Then
name|mockEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|jobExecution
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|shouldThrowExceptionIfUsedAsConsumer ()
specifier|public
name|void
name|shouldThrowExceptionIfUsedAsConsumer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// When
name|context
argument_list|()
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"spring-batch:mockJob"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:emptyEndpoint"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConvertHeadersToJobParams ()
specifier|public
name|void
name|shouldConvertHeadersToJobParams
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|String
name|headerKey
init|=
literal|"headerKey"
decl_stmt|;
name|String
name|headerValue
init|=
literal|"headerValue"
decl_stmt|;
comment|// When
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Start the job, please."
argument_list|,
name|headerKey
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
comment|// Then
name|ArgumentCaptor
argument_list|<
name|JobParameters
argument_list|>
name|jobParameters
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|JobParameters
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|jobLauncher
argument_list|)
operator|.
name|run
argument_list|(
name|any
argument_list|(
name|Job
operator|.
name|class
argument_list|)
argument_list|,
name|jobParameters
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|parameter
init|=
name|jobParameters
operator|.
name|getValue
argument_list|()
operator|.
name|getString
argument_list|(
name|headerKey
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|parameter
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setNullValueToJobParams ()
specifier|public
name|void
name|setNullValueToJobParams
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|String
name|headerKey
init|=
literal|"headerKey"
decl_stmt|;
name|Date
name|headerValue
init|=
literal|null
decl_stmt|;
comment|// When
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Start the job, please."
argument_list|,
name|headerKey
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
comment|// Then
name|ArgumentCaptor
argument_list|<
name|JobParameters
argument_list|>
name|jobParameters
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|JobParameters
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|jobLauncher
argument_list|)
operator|.
name|run
argument_list|(
name|any
argument_list|(
name|Job
operator|.
name|class
argument_list|)
argument_list|,
name|jobParameters
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|Date
name|parameter
init|=
name|jobParameters
operator|.
name|getValue
argument_list|()
operator|.
name|getDate
argument_list|(
name|headerKey
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|parameter
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConvertDateHeadersToJobParams ()
specifier|public
name|void
name|shouldConvertDateHeadersToJobParams
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|String
name|headerKey
init|=
literal|"headerKey"
decl_stmt|;
name|Date
name|headerValue
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
comment|// When
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Start the job, please."
argument_list|,
name|headerKey
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
comment|// Then
name|ArgumentCaptor
argument_list|<
name|JobParameters
argument_list|>
name|jobParameters
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|JobParameters
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|jobLauncher
argument_list|)
operator|.
name|run
argument_list|(
name|any
argument_list|(
name|Job
operator|.
name|class
argument_list|)
argument_list|,
name|jobParameters
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|Date
name|parameter
init|=
name|jobParameters
operator|.
name|getValue
argument_list|()
operator|.
name|getDate
argument_list|(
name|headerKey
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|parameter
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConvertLongHeadersToJobParams ()
specifier|public
name|void
name|shouldConvertLongHeadersToJobParams
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|String
name|headerKey
init|=
literal|"headerKey"
decl_stmt|;
name|Long
name|headerValue
init|=
literal|1L
decl_stmt|;
comment|// When
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Start the job, please."
argument_list|,
name|headerKey
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
comment|// Then
name|ArgumentCaptor
argument_list|<
name|JobParameters
argument_list|>
name|jobParameters
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|JobParameters
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|jobLauncher
argument_list|)
operator|.
name|run
argument_list|(
name|any
argument_list|(
name|Job
operator|.
name|class
argument_list|)
argument_list|,
name|jobParameters
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|parameter
init|=
name|jobParameters
operator|.
name|getValue
argument_list|()
operator|.
name|getLong
argument_list|(
name|headerKey
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|parameter
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConvertDoubleHeadersToJobParams ()
specifier|public
name|void
name|shouldConvertDoubleHeadersToJobParams
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|String
name|headerKey
init|=
literal|"headerKey"
decl_stmt|;
name|Double
name|headerValue
init|=
literal|1.0
decl_stmt|;
comment|// When
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Start the job, please."
argument_list|,
name|headerKey
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
comment|// Then
name|ArgumentCaptor
argument_list|<
name|JobParameters
argument_list|>
name|jobParameters
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|JobParameters
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|jobLauncher
argument_list|)
operator|.
name|run
argument_list|(
name|any
argument_list|(
name|Job
operator|.
name|class
argument_list|)
argument_list|,
name|jobParameters
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|Double
name|parameter
init|=
name|jobParameters
operator|.
name|getValue
argument_list|()
operator|.
name|getDouble
argument_list|(
name|headerKey
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|parameter
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldInjectJobLauncherByReferenceName ()
specifier|public
name|void
name|shouldInjectJobLauncherByReferenceName
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|context
argument_list|()
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:launcherRefTest"
argument_list|)
operator|.
name|to
argument_list|(
literal|"spring-batch:mockJob?jobLauncherRef=alternativeJobLauncher"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// When
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:launcherRefTest"
argument_list|,
literal|"Start the job, please."
argument_list|)
expr_stmt|;
comment|// Then
name|SpringBatchEndpoint
name|batchEndpoint
init|=
name|context
argument_list|()
operator|.
name|getEndpoint
argument_list|(
literal|"spring-batch:mockJob?jobLauncherRef=alternativeJobLauncher"
argument_list|,
name|SpringBatchEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|JobLauncher
name|batchEndpointJobLauncher
init|=
operator|(
name|JobLauncher
operator|)
name|FieldUtils
operator|.
name|readField
argument_list|(
name|batchEndpoint
argument_list|,
literal|"jobLauncher"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|alternativeJobLauncher
argument_list|,
name|batchEndpointJobLauncher
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FailedToCreateRouteException
operator|.
name|class
argument_list|)
DECL|method|shouldFailWhenThereIsNoJobLauncher ()
specifier|public
name|void
name|shouldFailWhenThereIsNoJobLauncher
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"mockJob"
argument_list|,
name|job
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"spring-batch:mockJob"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// When
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|FailedToCreateRouteException
operator|.
name|class
argument_list|)
DECL|method|shouldFailWhenThereIsMoreThanOneJobLauncher ()
specifier|public
name|void
name|shouldFailWhenThereIsMoreThanOneJobLauncher
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"mockJob"
argument_list|,
name|job
argument_list|)
expr_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"launcher1"
argument_list|,
name|jobLauncher
argument_list|)
expr_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"launcher2"
argument_list|,
name|jobLauncher
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"spring-batch:mockJob"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// When
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldResolveAnyJobLauncher ()
specifier|public
name|void
name|shouldResolveAnyJobLauncher
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"mockJob"
argument_list|,
name|job
argument_list|)
expr_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"someRandomName"
argument_list|,
name|jobLauncher
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"spring-batch:mockJob"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// When
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// Then
name|SpringBatchEndpoint
name|batchEndpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"spring-batch:mockJob"
argument_list|,
name|SpringBatchEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|JobLauncher
name|batchEndpointJobLauncher
init|=
operator|(
name|JobLauncher
operator|)
name|FieldUtils
operator|.
name|readField
argument_list|(
name|batchEndpoint
argument_list|,
literal|"jobLauncher"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|jobLauncher
argument_list|,
name|batchEndpointJobLauncher
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldUseJobLauncherFromComponent ()
specifier|public
name|void
name|shouldUseJobLauncherFromComponent
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|SpringBatchComponent
name|batchComponent
init|=
operator|new
name|SpringBatchComponent
argument_list|()
decl_stmt|;
name|batchComponent
operator|.
name|setJobLauncher
argument_list|(
name|alternativeJobLauncher
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"customBatchComponent"
argument_list|,
name|batchComponent
argument_list|)
expr_stmt|;
comment|// When
name|context
argument_list|()
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:startCustom"
argument_list|)
operator|.
name|to
argument_list|(
literal|"customBatchComponent:mockJob"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// Then
name|SpringBatchEndpoint
name|batchEndpoint
init|=
name|context
argument_list|()
operator|.
name|getEndpoint
argument_list|(
literal|"customBatchComponent:mockJob"
argument_list|,
name|SpringBatchEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|JobLauncher
name|batchEndpointJobLauncher
init|=
operator|(
name|JobLauncher
operator|)
name|FieldUtils
operator|.
name|readField
argument_list|(
name|batchEndpoint
argument_list|,
literal|"jobLauncher"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|alternativeJobLauncher
argument_list|,
name|batchEndpointJobLauncher
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

