begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|FileUtilTest
specifier|public
class|class
name|FileUtilTest
extends|extends
name|TestCase
block|{
DECL|method|testNormalizePath ()
specifier|public
name|void
name|testNormalizePath
parameter_list|()
block|{
if|if
condition|(
name|FileUtil
operator|.
name|isWindows
argument_list|()
condition|)
block|{
name|assertEquals
argument_list|(
literal|"foo\\bar"
argument_list|,
name|FileUtil
operator|.
name|normalizePath
argument_list|(
literal|"foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo\\bar\\baz"
argument_list|,
name|FileUtil
operator|.
name|normalizePath
argument_list|(
literal|"foo/bar\\baz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"foo/bar"
argument_list|,
name|FileUtil
operator|.
name|normalizePath
argument_list|(
literal|"foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo/bar/baz"
argument_list|,
name|FileUtil
operator|.
name|normalizePath
argument_list|(
literal|"foo/bar\\baz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testStripLeadingSeparator ()
specifier|public
name|void
name|testStripLeadingSeparator
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
literal|"foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo/"
argument_list|,
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
literal|"foo/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
literal|"/foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
literal|"//foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
literal|"///foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testHasLeadingSeparator ()
specifier|public
name|void
name|testHasLeadingSeparator
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|FileUtil
operator|.
name|hasLeadingSeparator
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|FileUtil
operator|.
name|hasLeadingSeparator
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|FileUtil
operator|.
name|hasLeadingSeparator
argument_list|(
literal|"foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|FileUtil
operator|.
name|hasLeadingSeparator
argument_list|(
literal|"foo/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|FileUtil
operator|.
name|hasLeadingSeparator
argument_list|(
literal|"/foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|FileUtil
operator|.
name|hasLeadingSeparator
argument_list|(
literal|"//foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|FileUtil
operator|.
name|hasLeadingSeparator
argument_list|(
literal|"///foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testStripFirstLeadingSeparator ()
specifier|public
name|void
name|testStripFirstLeadingSeparator
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|FileUtil
operator|.
name|stripFirstLeadingSeparator
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|FileUtil
operator|.
name|stripFirstLeadingSeparator
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripFirstLeadingSeparator
argument_list|(
literal|"foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo/"
argument_list|,
name|FileUtil
operator|.
name|stripFirstLeadingSeparator
argument_list|(
literal|"foo/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripFirstLeadingSeparator
argument_list|(
literal|"/foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripFirstLeadingSeparator
argument_list|(
literal|"//foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"//foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripFirstLeadingSeparator
argument_list|(
literal|"///foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testStripTrailingSeparator ()
specifier|public
name|void
name|testStripTrailingSeparator
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
literal|"foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
literal|"foo/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
literal|"foo/bar/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
literal|"/foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
literal|"/foo/bar/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
literal|"/foo/bar//"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
literal|"/foo/bar///"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/foo"
argument_list|,
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
literal|"/foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/foo"
argument_list|,
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
literal|"/foo/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"//"
argument_list|,
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
literal|"//"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testStripPath ()
specifier|public
name|void
name|testStripPath
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|FileUtil
operator|.
name|stripPath
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|FileUtil
operator|.
name|stripPath
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo.xml"
argument_list|,
name|FileUtil
operator|.
name|stripPath
argument_list|(
literal|"/foo.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|FileUtil
operator|.
name|stripPath
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|FileUtil
operator|.
name|stripPath
argument_list|(
literal|"foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|FileUtil
operator|.
name|stripPath
argument_list|(
literal|"/foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testStripPathWithMixedSeparators ()
specifier|public
name|void
name|testStripPathWithMixedSeparators
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|FileUtil
operator|.
name|stripPath
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|FileUtil
operator|.
name|stripPath
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo.xml"
argument_list|,
name|FileUtil
operator|.
name|stripPath
argument_list|(
literal|"/foo.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|FileUtil
operator|.
name|stripPath
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"baz"
argument_list|,
name|FileUtil
operator|.
name|stripPath
argument_list|(
literal|"foo/bar\\baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|FileUtil
operator|.
name|stripPath
argument_list|(
literal|"\\foo\\bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"baz"
argument_list|,
name|FileUtil
operator|.
name|stripPath
argument_list|(
literal|"/foo\\bar/baz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testStripExt ()
specifier|public
name|void
name|testStripExt
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|FileUtil
operator|.
name|stripExt
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|FileUtil
operator|.
name|stripExt
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|FileUtil
operator|.
name|stripExt
argument_list|(
literal|"foo.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/foo/bar"
argument_list|,
name|FileUtil
operator|.
name|stripExt
argument_list|(
literal|"/foo/bar.xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testOnlyPath ()
specifier|public
name|void
name|testOnlyPath
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|"foo.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|"foo/bar.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/foo"
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|"/foo/bar.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/foo/bar"
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|"/foo/bar/baz.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|"/foo.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/bar"
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|"/bar/foo.xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testOnlyPathWithMixedSeparators ()
specifier|public
name|void
name|testOnlyPathWithMixedSeparators
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|"foo.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|"foo/bar.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/foo"
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|"/foo\\bar.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"\\foo\\bar"
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|"\\foo\\bar/baz.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"\\"
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|"\\foo.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/bar"
argument_list|,
name|FileUtil
operator|.
name|onlyPath
argument_list|(
literal|"/bar\\foo.xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCompactPath ()
specifier|public
name|void
name|testCompactPath
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|FileUtil
operator|.
name|isWindows
argument_list|()
condition|)
block|{
name|assertEquals
argument_list|(
literal|"..\\foo"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"..\\foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"..\\..\\foo"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"..\\..\\foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"..\\..\\foo\\bar"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"..\\..\\foo\\bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"..\\..\\foo"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"..\\..\\foo\\bar\\.."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"foo\\..\\bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar\\baz"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"foo\\..\\bar\\baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo\\baz"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"foo\\bar\\..\\baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"baz"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"foo\\bar\\..\\..\\baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"..\\baz"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"foo\\bar\\..\\..\\..\\baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"..\\foo\\bar"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"..\\foo\\bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
literal|"../foo"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"../foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"../../foo"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"../../foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"../../foo/bar"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"../../foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"../../foo"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"../../foo/bar/.."
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"foo/../bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar/baz"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"foo/../bar/baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo/baz"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"foo/bar/../baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"baz"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"foo/bar/../../baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"../baz"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"foo/bar/../../../baz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"../foo/bar"
argument_list|,
name|FileUtil
operator|.
name|compactPath
argument_list|(
literal|"../foo/bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testDefaultTempFileSuffixAndPrefix ()
specifier|public
name|void
name|testDefaultTempFileSuffixAndPrefix
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|tmp
init|=
name|FileUtil
operator|.
name|createTempFile
argument_list|(
literal|"tmp-"
argument_list|,
literal|".tmp"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be a file"
argument_list|,
name|tmp
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDefaultTempFile ()
specifier|public
name|void
name|testDefaultTempFile
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|tmp
init|=
name|FileUtil
operator|.
name|createTempFile
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be a file"
argument_list|,
name|tmp
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDefaultTempFileParent ()
specifier|public
name|void
name|testDefaultTempFileParent
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|tmp
init|=
name|FileUtil
operator|.
name|createTempFile
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|File
argument_list|(
literal|"target"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be a file"
argument_list|,
name|tmp
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

