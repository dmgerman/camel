begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.apns.factory
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|apns
operator|.
name|factory
package|;
end_package

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|ApnsService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|utils
operator|.
name|FixedCertificates
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
name|apns
operator|.
name|model
operator|.
name|ConnectionStrategy
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
name|apns
operator|.
name|util
operator|.
name|ApnsUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_class
DECL|class|ApnsServiceFactoryTest
specifier|public
class|class
name|ApnsServiceFactoryTest
block|{
annotation|@
name|Test
DECL|method|testApnsServiceFactoryWithFixedCertificates ()
specifier|public
name|void
name|testApnsServiceFactoryWithFixedCertificates
parameter_list|()
throws|throws
name|Exception
block|{
name|ApnsServiceFactory
name|apnsServiceFactory
init|=
name|createApnsServiceFactoryWithFixedCertificates
argument_list|()
decl_stmt|;
name|ApnsService
name|apnsService
init|=
name|apnsServiceFactory
operator|.
name|getApnsService
argument_list|()
decl_stmt|;
name|doBasicAsserts
argument_list|(
name|apnsService
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
DECL|method|testApnsServiceFactoryAsPool0 ()
specifier|public
name|void
name|testApnsServiceFactoryAsPool0
parameter_list|()
throws|throws
name|Exception
block|{
name|ApnsServiceFactory
name|apnsServiceFactory
init|=
name|createApnsServiceFactoryWithFixedCertificatesAsPool
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ApnsService
name|apnsService
init|=
name|apnsServiceFactory
operator|.
name|getApnsService
argument_list|()
decl_stmt|;
name|doBasicAsserts
argument_list|(
name|apnsService
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testApnsServiceFactoryAsPool1 ()
specifier|public
name|void
name|testApnsServiceFactoryAsPool1
parameter_list|()
throws|throws
name|Exception
block|{
name|ApnsServiceFactory
name|apnsServiceFactory
init|=
name|createApnsServiceFactoryWithFixedCertificatesAsPool
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|ApnsService
name|apnsService
init|=
name|apnsServiceFactory
operator|.
name|getApnsService
argument_list|()
decl_stmt|;
name|doBasicAsserts
argument_list|(
name|apnsService
argument_list|)
expr_stmt|;
block|}
DECL|method|doBasicAsserts (Object apnsService)
specifier|private
name|void
name|doBasicAsserts
parameter_list|(
name|Object
name|apnsService
parameter_list|)
block|{
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|apnsService
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|apnsService
operator|instanceof
name|ApnsService
argument_list|)
expr_stmt|;
block|}
DECL|method|createApnsServiceFactoryWithFixedCertificates ()
specifier|public
specifier|static
name|ApnsServiceFactory
name|createApnsServiceFactoryWithFixedCertificates
parameter_list|()
throws|throws
name|Exception
block|{
name|ApnsServiceFactory
name|apnsServiceFactory
init|=
operator|new
name|ApnsServiceFactory
argument_list|()
decl_stmt|;
name|apnsServiceFactory
operator|.
name|setFeedbackHost
argument_list|(
name|FixedCertificates
operator|.
name|TEST_HOST
argument_list|)
expr_stmt|;
name|apnsServiceFactory
operator|.
name|setFeedbackPort
argument_list|(
name|FixedCertificates
operator|.
name|TEST_FEEDBACK_PORT
argument_list|)
expr_stmt|;
name|apnsServiceFactory
operator|.
name|setGatewayHost
argument_list|(
name|FixedCertificates
operator|.
name|TEST_HOST
argument_list|)
expr_stmt|;
name|apnsServiceFactory
operator|.
name|setGatewayPort
argument_list|(
name|FixedCertificates
operator|.
name|TEST_GATEWAY_PORT
argument_list|)
expr_stmt|;
name|apnsServiceFactory
operator|.
name|setSslContext
argument_list|(
name|ApnsUtils
operator|.
name|clientContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|apnsServiceFactory
return|;
block|}
DECL|method|createApnsServiceFactoryWithFixedCertificatesAsPool (int poolSize)
specifier|private
name|ApnsServiceFactory
name|createApnsServiceFactoryWithFixedCertificatesAsPool
parameter_list|(
name|int
name|poolSize
parameter_list|)
throws|throws
name|Exception
block|{
name|ApnsServiceFactory
name|apnsServiceFactory
init|=
name|createApnsServiceFactoryWithFixedCertificates
argument_list|()
decl_stmt|;
name|apnsServiceFactory
operator|.
name|setConnectionStrategy
argument_list|(
name|ConnectionStrategy
operator|.
name|POOL
argument_list|)
expr_stmt|;
name|apnsServiceFactory
operator|.
name|setPoolSize
argument_list|(
name|poolSize
argument_list|)
expr_stmt|;
return|return
name|apnsServiceFactory
return|;
block|}
block|}
end_class

end_unit

