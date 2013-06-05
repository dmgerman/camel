begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|bulk
operator|.
name|ContentType
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|bulk
operator|.
name|JobInfo
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|bulk
operator|.
name|JobStateEnum
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
name|salesforce
operator|.
name|api
operator|.
name|dto
operator|.
name|bulk
operator|.
name|OperationEnum
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
name|salesforce
operator|.
name|dto
operator|.
name|Merchandise__c
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|experimental
operator|.
name|theories
operator|.
name|DataPoints
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|experimental
operator|.
name|theories
operator|.
name|Theory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|BulkApiJobIntegrationTest
specifier|public
class|class
name|BulkApiJobIntegrationTest
extends|extends
name|AbstractBulkApiTestBase
block|{
comment|// test jobs for testJobLifecycle
annotation|@
name|DataPoints
DECL|method|getJobs ()
specifier|public
specifier|static
name|JobInfo
index|[]
name|getJobs
parameter_list|()
block|{
name|JobInfo
name|jobInfo
init|=
operator|new
name|JobInfo
argument_list|()
decl_stmt|;
comment|// insert XML
name|jobInfo
operator|.
name|setObject
argument_list|(
name|Merchandise__c
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setContentType
argument_list|(
name|ContentType
operator|.
name|XML
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setOperation
argument_list|(
name|OperationEnum
operator|.
name|INSERT
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|JobInfo
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|JobInfo
argument_list|>
argument_list|()
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|jobInfo
argument_list|)
expr_stmt|;
comment|// insert CSV
name|jobInfo
operator|=
operator|new
name|JobInfo
argument_list|()
expr_stmt|;
name|jobInfo
operator|.
name|setObject
argument_list|(
name|Merchandise__c
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setContentType
argument_list|(
name|ContentType
operator|.
name|CSV
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setOperation
argument_list|(
name|OperationEnum
operator|.
name|INSERT
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|jobInfo
argument_list|)
expr_stmt|;
comment|// update CSV
name|jobInfo
operator|=
operator|new
name|JobInfo
argument_list|()
expr_stmt|;
name|jobInfo
operator|.
name|setObject
argument_list|(
name|Merchandise__c
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setContentType
argument_list|(
name|ContentType
operator|.
name|CSV
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setOperation
argument_list|(
name|OperationEnum
operator|.
name|UPDATE
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|jobInfo
argument_list|)
expr_stmt|;
comment|// upsert CSV
name|jobInfo
operator|=
operator|new
name|JobInfo
argument_list|()
expr_stmt|;
name|jobInfo
operator|.
name|setObject
argument_list|(
name|Merchandise__c
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setContentType
argument_list|(
name|ContentType
operator|.
name|CSV
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setOperation
argument_list|(
name|OperationEnum
operator|.
name|UPSERT
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setExternalIdFieldName
argument_list|(
literal|"Name"
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|jobInfo
argument_list|)
expr_stmt|;
comment|// delete CSV
name|jobInfo
operator|=
operator|new
name|JobInfo
argument_list|()
expr_stmt|;
name|jobInfo
operator|.
name|setObject
argument_list|(
name|Merchandise__c
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setContentType
argument_list|(
name|ContentType
operator|.
name|CSV
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setOperation
argument_list|(
name|OperationEnum
operator|.
name|DELETE
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|jobInfo
argument_list|)
expr_stmt|;
comment|// hard delete CSV
name|jobInfo
operator|=
operator|new
name|JobInfo
argument_list|()
expr_stmt|;
name|jobInfo
operator|.
name|setObject
argument_list|(
name|Merchandise__c
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setContentType
argument_list|(
name|ContentType
operator|.
name|CSV
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setOperation
argument_list|(
name|OperationEnum
operator|.
name|HARD_DELETE
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|jobInfo
argument_list|)
expr_stmt|;
comment|// query CSV
name|jobInfo
operator|=
operator|new
name|JobInfo
argument_list|()
expr_stmt|;
name|jobInfo
operator|.
name|setObject
argument_list|(
name|Merchandise__c
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setContentType
argument_list|(
name|ContentType
operator|.
name|CSV
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setOperation
argument_list|(
name|OperationEnum
operator|.
name|QUERY
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|jobInfo
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|toArray
argument_list|(
operator|new
name|JobInfo
index|[
name|result
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
annotation|@
name|Theory
DECL|method|testJobLifecycle (JobInfo jobInfo)
specifier|public
name|void
name|testJobLifecycle
parameter_list|(
name|JobInfo
name|jobInfo
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Testing Job lifecycle for {} of type {}"
argument_list|,
name|jobInfo
operator|.
name|getOperation
argument_list|()
argument_list|,
name|jobInfo
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
comment|// test create
name|jobInfo
operator|=
name|createJob
argument_list|(
name|jobInfo
argument_list|)
expr_stmt|;
comment|// test get
name|jobInfo
operator|=
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:getJob"
argument_list|,
name|jobInfo
argument_list|,
name|JobInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Job should be OPEN"
argument_list|,
name|JobStateEnum
operator|.
name|OPEN
argument_list|,
name|jobInfo
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
comment|// test close
name|jobInfo
operator|=
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:closeJob"
argument_list|,
name|jobInfo
argument_list|,
name|JobInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Job should be CLOSED"
argument_list|,
name|JobStateEnum
operator|.
name|CLOSED
argument_list|,
name|jobInfo
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
comment|// test abort
name|jobInfo
operator|=
name|template
argument_list|()
operator|.
name|requestBody
argument_list|(
literal|"direct:abortJob"
argument_list|,
name|jobInfo
argument_list|,
name|JobInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"Job should be ABORTED"
argument_list|,
name|JobStateEnum
operator|.
name|ABORTED
argument_list|,
name|jobInfo
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

