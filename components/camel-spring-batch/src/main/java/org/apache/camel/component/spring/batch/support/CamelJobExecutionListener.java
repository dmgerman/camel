begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.batch.support
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
operator|.
name|support
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
name|ProducerTemplate
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
name|JobExecutionListener
import|;
end_import

begin_class
DECL|class|CamelJobExecutionListener
specifier|public
class|class
name|CamelJobExecutionListener
implements|implements
name|JobExecutionListener
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
name|CamelJobExecutionListener
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|producerTemplate
specifier|private
specifier|final
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
DECL|field|endpointUri
specifier|private
specifier|final
name|String
name|endpointUri
decl_stmt|;
DECL|method|CamelJobExecutionListener (ProducerTemplate producerTemplate, String endpointUri)
specifier|public
name|CamelJobExecutionListener
parameter_list|(
name|ProducerTemplate
name|producerTemplate
parameter_list|,
name|String
name|endpointUri
parameter_list|)
block|{
name|this
operator|.
name|producerTemplate
operator|=
name|producerTemplate
expr_stmt|;
name|this
operator|.
name|endpointUri
operator|=
name|endpointUri
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeJob (JobExecution jobExecution)
specifier|public
name|void
name|beforeJob
parameter_list|(
name|JobExecution
name|jobExecution
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"sending before job execution event [{}]..."
argument_list|,
name|jobExecution
argument_list|)
expr_stmt|;
name|producerTemplate
operator|.
name|sendBodyAndHeader
argument_list|(
name|endpointUri
argument_list|,
name|jobExecution
argument_list|,
name|EventType
operator|.
name|HEADER_KEY
argument_list|,
name|EventType
operator|.
name|BEFORE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"sent before job execution event"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterJob (JobExecution jobExecution)
specifier|public
name|void
name|afterJob
parameter_list|(
name|JobExecution
name|jobExecution
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"sending after job execution event [{}]..."
argument_list|,
name|jobExecution
argument_list|)
expr_stmt|;
name|producerTemplate
operator|.
name|sendBodyAndHeader
argument_list|(
name|endpointUri
argument_list|,
name|jobExecution
argument_list|,
name|EventType
operator|.
name|HEADER_KEY
argument_list|,
name|EventType
operator|.
name|AFTER
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"sent after job execution event"
argument_list|)
expr_stmt|;
block|}
DECL|enum|EventType
specifier|public
enum|enum
name|EventType
block|{
DECL|enumConstant|BEFORE
DECL|enumConstant|AFTER
name|BEFORE
block|,
name|AFTER
block|;
DECL|field|HEADER_KEY
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_KEY
init|=
literal|"SPRING_BATCH_JOB_EVENT_TYPE"
decl_stmt|;
block|}
block|}
end_class

end_unit

