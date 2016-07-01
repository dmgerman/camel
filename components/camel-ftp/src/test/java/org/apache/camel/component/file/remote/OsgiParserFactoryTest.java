begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|remote
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|net
operator|.
name|ftp
operator|.
name|FTPClientConfig
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
name|net
operator|.
name|ftp
operator|.
name|FTPFileEntryParser
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
name|net
operator|.
name|ftp
operator|.
name|parser
operator|.
name|CompositeFileEntryParser
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
name|net
operator|.
name|ftp
operator|.
name|parser
operator|.
name|NTFTPEntryParser
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
name|net
operator|.
name|ftp
operator|.
name|parser
operator|.
name|UnixFTPEntryParser
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
name|net
operator|.
name|ftp
operator|.
name|parser
operator|.
name|VMSVersioningFTPEntryParser
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
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
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
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|IsInstanceOf
operator|.
name|instanceOf
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
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
name|when
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
DECL|class|OsgiParserFactoryTest
specifier|public
class|class
name|OsgiParserFactoryTest
block|{
DECL|field|OSGI_PARSER_FACTORY
specifier|private
specifier|static
specifier|final
name|OsgiParserFactory
name|OSGI_PARSER_FACTORY
init|=
operator|new
name|OsgiParserFactory
argument_list|(
literal|null
argument_list|)
decl_stmt|;
annotation|@
name|Mock
DECL|field|ftpClientConfig
specifier|private
name|FTPClientConfig
name|ftpClientConfig
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
name|when
argument_list|(
name|ftpClientConfig
operator|.
name|getDefaultDateFormatStr
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"yyyy-MM-dd"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createFileEntryParserUnix ()
specifier|public
name|void
name|createFileEntryParserUnix
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|ftpClientConfig
operator|.
name|getServerSystemKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"bla unix bla"
argument_list|)
expr_stmt|;
name|FTPFileEntryParser
name|result
init|=
name|OSGI_PARSER_FACTORY
operator|.
name|createFileEntryParser
argument_list|(
name|ftpClientConfig
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|instanceOf
argument_list|(
name|UnixFTPEntryParser
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createFileEntryParserLinux ()
specifier|public
name|void
name|createFileEntryParserLinux
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|ftpClientConfig
operator|.
name|getServerSystemKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"bla linux bla"
argument_list|)
expr_stmt|;
name|FTPFileEntryParser
name|result
init|=
name|OSGI_PARSER_FACTORY
operator|.
name|createFileEntryParser
argument_list|(
name|ftpClientConfig
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|instanceOf
argument_list|(
name|UnixFTPEntryParser
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createFileEntryParserTypeL8 ()
specifier|public
name|void
name|createFileEntryParserTypeL8
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|ftpClientConfig
operator|.
name|getServerSystemKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"bla type: l8 bla"
argument_list|)
expr_stmt|;
name|FTPFileEntryParser
name|result
init|=
name|OSGI_PARSER_FACTORY
operator|.
name|createFileEntryParser
argument_list|(
name|ftpClientConfig
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|instanceOf
argument_list|(
name|UnixFTPEntryParser
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createFileEntryParserVms ()
specifier|public
name|void
name|createFileEntryParserVms
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|ftpClientConfig
operator|.
name|getServerSystemKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"bla vms bla"
argument_list|)
expr_stmt|;
name|FTPFileEntryParser
name|result
init|=
name|OSGI_PARSER_FACTORY
operator|.
name|createFileEntryParser
argument_list|(
name|ftpClientConfig
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|instanceOf
argument_list|(
name|VMSVersioningFTPEntryParser
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createFileEntryParserPlainWindows ()
specifier|public
name|void
name|createFileEntryParserPlainWindows
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|ftpClientConfig
operator|.
name|getServerSystemKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"WINDOWS"
argument_list|)
expr_stmt|;
name|FTPFileEntryParser
name|result
init|=
name|OSGI_PARSER_FACTORY
operator|.
name|createFileEntryParser
argument_list|(
name|ftpClientConfig
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|instanceOf
argument_list|(
name|NTFTPEntryParser
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createFileEntryParserNotPlainWindows ()
specifier|public
name|void
name|createFileEntryParserNotPlainWindows
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|ftpClientConfig
operator|.
name|getServerSystemKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"WINDOWS XP"
argument_list|)
expr_stmt|;
name|FTPFileEntryParser
name|result
init|=
name|OSGI_PARSER_FACTORY
operator|.
name|createFileEntryParser
argument_list|(
name|ftpClientConfig
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|instanceOf
argument_list|(
name|CompositeFileEntryParser
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createFileEntryParserWin32 ()
specifier|public
name|void
name|createFileEntryParserWin32
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|ftpClientConfig
operator|.
name|getServerSystemKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"WIN32"
argument_list|)
expr_stmt|;
name|FTPFileEntryParser
name|result
init|=
name|OSGI_PARSER_FACTORY
operator|.
name|createFileEntryParser
argument_list|(
name|ftpClientConfig
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|,
name|instanceOf
argument_list|(
name|CompositeFileEntryParser
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

