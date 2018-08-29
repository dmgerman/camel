begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hawtdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hawtdb
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|Exchange
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
name|DefaultExchange
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

begin_class
DECL|class|HawtDBExchangeSerializationTest
specifier|public
class|class
name|HawtDBExchangeSerializationTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|hawtDBFile
specifier|private
name|HawtDBFile
name|hawtDBFile
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteDirectory
argument_list|(
literal|"target/data"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/data/hawtdb.dat"
argument_list|)
decl_stmt|;
name|hawtDBFile
operator|=
operator|new
name|HawtDBFile
argument_list|()
expr_stmt|;
name|hawtDBFile
operator|.
name|setFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|hawtDBFile
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|hawtDBFile
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExchangeSerialization ()
specifier|public
name|void
name|testExchangeSerialization
parameter_list|()
block|{
name|HawtDBAggregationRepository
name|repo
init|=
operator|new
name|HawtDBAggregationRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setHawtDBFile
argument_list|(
name|hawtDBFile
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setRepositoryName
argument_list|(
literal|"repo1"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"name"
argument_list|,
literal|"Claus"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"number"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"quote"
argument_list|,
literal|"Camel rocks"
argument_list|)
expr_stmt|;
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"date"
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|repo
operator|.
name|add
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|Exchange
name|actual
init|=
name|repo
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"number"
argument_list|)
argument_list|)
expr_stmt|;
name|Date
name|date
init|=
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"date"
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|date
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|now
operator|.
name|getTime
argument_list|()
argument_list|,
name|date
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
comment|// we do not serialize properties to avoid storing all kind of not needed information
name|assertNull
argument_list|(
name|actual
operator|.
name|getProperty
argument_list|(
literal|"quote"
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|actual
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
comment|// change something
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"name"
argument_list|,
literal|"Hiram"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
literal|"date"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|add
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|actual
operator|=
name|repo
operator|.
name|get
argument_list|(
name|context
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hiram"
argument_list|,
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"number"
argument_list|)
argument_list|)
expr_stmt|;
name|date
operator|=
name|actual
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"date"
argument_list|,
name|Date
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|date
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|context
argument_list|,
name|actual
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

