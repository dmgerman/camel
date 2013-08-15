begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.fix
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|fix
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|dataformat
operator|.
name|bindy
operator|.
name|BindyKeyValuePairFactory
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
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|fix
operator|.
name|complex
operator|.
name|onetomany
operator|.
name|Header
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
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|fix
operator|.
name|complex
operator|.
name|onetomany
operator|.
name|Order
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
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|fix
operator|.
name|complex
operator|.
name|onetomany
operator|.
name|Trailer
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
name|DefaultPackageScanClassResolver
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
name|spi
operator|.
name|PackageScanClassResolver
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
name|Before
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

begin_class
DECL|class|BindyComplexKeyValuePairStandaloneTest
specifier|public
class|class
name|BindyComplexKeyValuePairStandaloneTest
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
name|BindyComplexKeyValuePairStandaloneTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|model
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|models
specifier|protected
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|models
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|factory
name|BindyKeyValuePairFactory
name|factory
decl_stmt|;
DECL|field|counter
name|int
name|counter
decl_stmt|;
annotation|@
name|Before
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Set factory
name|PackageScanClassResolver
name|res
init|=
operator|new
name|DefaultPackageScanClassResolver
argument_list|()
decl_stmt|;
name|factory
operator|=
operator|new
name|BindyKeyValuePairFactory
argument_list|(
name|res
argument_list|,
literal|"org.apache.camel.dataformat.bindy.model.fix.complex.onetomany"
argument_list|)
expr_stmt|;
comment|// Set model class
name|models
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|fix
operator|.
name|complex
operator|.
name|onetomany
operator|.
name|Order
operator|.
name|class
argument_list|)
expr_stmt|;
name|models
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|fix
operator|.
name|complex
operator|.
name|onetomany
operator|.
name|Header
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// f.models.add(org.apache.camel.dataformat.bindy.model.fix.complex.onetomany.Security.class);
name|models
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|fix
operator|.
name|complex
operator|.
name|onetomany
operator|.
name|Trailer
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Init model
name|model
operator|.
name|put
argument_list|(
literal|"org.apache.camel.dataformat.bindy.model.fix.complex.onetomany.Order"
argument_list|,
operator|new
name|Order
argument_list|()
argument_list|)
expr_stmt|;
name|model
operator|.
name|put
argument_list|(
literal|"org.apache.camel.dataformat.bindy.model.fix.complex.onetomany.Header"
argument_list|,
operator|new
name|Header
argument_list|()
argument_list|)
expr_stmt|;
name|model
operator|.
name|put
argument_list|(
literal|"org.apache.camel.dataformat.bindy.model.fix.complex.onetomany.Trailer"
argument_list|,
operator|new
name|Trailer
argument_list|()
argument_list|)
expr_stmt|;
comment|// set counter = 1
name|counter
operator|=
literal|1
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testOneGroupMessage ()
specifier|public
name|void
name|testOneGroupMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|message
init|=
literal|"8=FIX 4.1
literal|9=20
literal|34=1
literal|35=0
literal|49=INVMGR
literal|56=BRKR"
operator|+
literal|"
literal|1=BE.CHM.001
literal|11=CHM0001-01
literal|58=this is a camel - bindy test"
operator|+
literal|"
literal|22=4
literal|48=BE0001245678
literal|54=1"
operator|+
literal|"
literal|10=220"
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|data
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|message
operator|.
name|split
argument_list|(
literal|"\\u0001"
argument_list|)
argument_list|)
decl_stmt|;
name|factory
operator|.
name|bind
argument_list|(
name|data
argument_list|,
name|model
argument_list|,
name|counter
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|">>> Model : "
operator|+
name|model
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSeveralGroupMessage ()
specifier|public
name|void
name|testSeveralGroupMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|message
init|=
literal|"8=FIX 4.1
literal|9=20
literal|34=1
literal|35=0
literal|49=INVMGR
literal|56=BRKR"
operator|+
literal|"
literal|1=BE.CHM.001
literal|11=CHM0001-01
literal|58=this is a camel - bindy test"
operator|+
literal|"
literal|22=4
literal|48=BE0001245678
literal|54=1"
operator|+
literal|"
literal|22=5
literal|48=BE0009876543
literal|54=2"
operator|+
literal|"
literal|22=6
literal|48=BE0009999999
literal|54=3"
operator|+
literal|"
literal|10=220"
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|data
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|message
operator|.
name|split
argument_list|(
literal|"\\u0001"
argument_list|)
argument_list|)
decl_stmt|;
name|factory
operator|.
name|bind
argument_list|(
name|data
argument_list|,
name|model
argument_list|,
name|counter
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|">>> Model : "
operator|+
name|model
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoGroupMessage ()
specifier|public
name|void
name|testNoGroupMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|message
init|=
literal|"8=FIX 4.1
literal|9=20
literal|34=1
literal|35=0
literal|49=INVMGR
literal|56=BRKR"
operator|+
literal|"
literal|1=BE.CHM.001
literal|11=CHM0001-01
literal|58=this is a camel - bindy test"
operator|+
literal|"
literal|10=220"
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|data
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|message
operator|.
name|split
argument_list|(
literal|"\\u0001"
argument_list|)
argument_list|)
decl_stmt|;
name|factory
operator|.
name|bind
argument_list|(
name|data
argument_list|,
name|model
argument_list|,
name|counter
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|">>> Model : "
operator|+
name|model
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

