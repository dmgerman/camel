begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.component.git
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|git
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|DefaultProducer
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|api
operator|.
name|Git
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|storage
operator|.
name|file
operator|.
name|FileRepositoryBuilder
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
DECL|class|GitProducer
specifier|public
class|class
name|GitProducer
extends|extends
name|DefaultProducer
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
name|GitProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|GitEndpoint
name|endpoint
decl_stmt|;
DECL|method|GitProducer (GitEndpoint endpoint)
specifier|public
name|GitProducer
parameter_list|(
name|GitEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|operation
decl_stmt|;
name|Repository
name|repo
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|endpoint
operator|.
name|getOperation
argument_list|()
argument_list|)
condition|)
block|{
name|operation
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GitConstants
operator|.
name|GIT_OPERATION
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|operation
operator|=
name|endpoint
operator|.
name|getOperation
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|endpoint
operator|.
name|getLocalPath
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Local path must specified to execute "
operator|+
name|operation
argument_list|)
throw|;
block|}
name|repo
operator|=
name|getLocalRepository
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|operation
condition|)
block|{
case|case
name|GitOperation
operator|.
name|CLONE_OPERATION
case|:
name|doClone
argument_list|(
name|exchange
argument_list|,
name|operation
argument_list|)
expr_stmt|;
break|break;
case|case
name|GitOperation
operator|.
name|INIT_OPERATION
case|:
name|doInit
argument_list|(
name|exchange
argument_list|,
name|operation
argument_list|)
expr_stmt|;
break|break;
case|case
name|GitOperation
operator|.
name|ADD_OPERATION
case|:
name|doAdd
argument_list|(
name|exchange
argument_list|,
name|operation
argument_list|,
name|repo
argument_list|)
expr_stmt|;
break|break;
case|case
name|GitOperation
operator|.
name|COMMIT_OPERATION
case|:
name|doCommit
argument_list|(
name|exchange
argument_list|,
name|operation
argument_list|,
name|repo
argument_list|)
expr_stmt|;
break|break;
case|case
name|GitOperation
operator|.
name|COMMIT_ALL_OPERATION
case|:
name|doCommitAll
argument_list|(
name|exchange
argument_list|,
name|operation
argument_list|,
name|repo
argument_list|)
expr_stmt|;
break|break;
block|}
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|doClone (Exchange exchange, String operation)
specifier|protected
name|void
name|doClone
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|operation
parameter_list|)
block|{
name|Git
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|endpoint
operator|.
name|getLocalPath
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Local path must specified to execute "
operator|+
name|operation
argument_list|)
throw|;
block|}
try|try
block|{
name|File
name|localRepo
init|=
operator|new
name|File
argument_list|(
name|endpoint
operator|.
name|getLocalPath
argument_list|()
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|localRepo
operator|.
name|exists
argument_list|()
condition|)
block|{
name|result
operator|=
name|Git
operator|.
name|cloneRepository
argument_list|()
operator|.
name|setURI
argument_list|(
name|endpoint
operator|.
name|getRemotePath
argument_list|()
argument_list|)
operator|.
name|setDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|endpoint
operator|.
name|getLocalPath
argument_list|()
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The local repository directory already exists"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"There was an error in Git "
operator|+
name|operation
operator|+
literal|" operation"
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|result
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doInit (Exchange exchange, String operation)
specifier|protected
name|void
name|doInit
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|operation
parameter_list|)
block|{
name|Git
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|endpoint
operator|.
name|getLocalPath
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Local path must specified to execute "
operator|+
name|operation
argument_list|)
throw|;
block|}
try|try
block|{
name|result
operator|=
name|Git
operator|.
name|init
argument_list|()
operator|.
name|setDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|endpoint
operator|.
name|getLocalPath
argument_list|()
argument_list|,
literal|""
argument_list|)
argument_list|)
operator|.
name|setBare
argument_list|(
literal|false
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"There was an error in Git "
operator|+
name|operation
operator|+
literal|" operation"
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|result
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doAdd (Exchange exchange, String operation, Repository repo)
specifier|protected
name|void
name|doAdd
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|operation
parameter_list|,
name|Repository
name|repo
parameter_list|)
block|{
name|Git
name|git
init|=
literal|null
decl_stmt|;
name|String
name|fileName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GitConstants
operator|.
name|GIT_FILE_NAME
argument_list|)
argument_list|)
condition|)
block|{
name|fileName
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GitConstants
operator|.
name|GIT_FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"File name must be specified to execute "
operator|+
name|operation
argument_list|)
throw|;
block|}
try|try
block|{
name|git
operator|=
operator|new
name|Git
argument_list|(
name|repo
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getBranchName
argument_list|()
argument_list|)
condition|)
block|{
name|git
operator|.
name|checkout
argument_list|()
operator|.
name|setCreateBranch
argument_list|(
literal|false
argument_list|)
operator|.
name|setName
argument_list|(
name|endpoint
operator|.
name|getBranchName
argument_list|()
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
name|git
operator|.
name|add
argument_list|()
operator|.
name|addFilepattern
argument_list|(
name|fileName
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"There was an error in Git "
operator|+
name|operation
operator|+
literal|" operation"
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doCommit (Exchange exchange, String operation, Repository repo)
specifier|protected
name|void
name|doCommit
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|operation
parameter_list|,
name|Repository
name|repo
parameter_list|)
block|{
name|Git
name|git
init|=
literal|null
decl_stmt|;
name|String
name|commitMessage
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GitConstants
operator|.
name|GIT_COMMIT_MESSAGE
argument_list|)
argument_list|)
condition|)
block|{
name|commitMessage
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GitConstants
operator|.
name|GIT_COMMIT_MESSAGE
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Commit message must be specified to execute "
operator|+
name|operation
argument_list|)
throw|;
block|}
try|try
block|{
name|git
operator|=
operator|new
name|Git
argument_list|(
name|repo
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getBranchName
argument_list|()
argument_list|)
condition|)
block|{
name|git
operator|.
name|checkout
argument_list|()
operator|.
name|setCreateBranch
argument_list|(
literal|false
argument_list|)
operator|.
name|setName
argument_list|(
name|endpoint
operator|.
name|getBranchName
argument_list|()
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
name|git
operator|.
name|commit
argument_list|()
operator|.
name|setMessage
argument_list|(
name|commitMessage
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"There was an error in Git "
operator|+
name|operation
operator|+
literal|" operation"
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doCommitAll (Exchange exchange, String operation, Repository repo)
specifier|protected
name|void
name|doCommitAll
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|operation
parameter_list|,
name|Repository
name|repo
parameter_list|)
block|{
name|Git
name|git
init|=
literal|null
decl_stmt|;
name|String
name|commitMessage
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GitConstants
operator|.
name|GIT_COMMIT_MESSAGE
argument_list|)
argument_list|)
condition|)
block|{
name|commitMessage
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GitConstants
operator|.
name|GIT_COMMIT_MESSAGE
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Commit message must be specified to execute "
operator|+
name|operation
argument_list|)
throw|;
block|}
try|try
block|{
name|git
operator|=
operator|new
name|Git
argument_list|(
name|repo
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getBranchName
argument_list|()
argument_list|)
condition|)
block|{
name|git
operator|.
name|checkout
argument_list|()
operator|.
name|setCreateBranch
argument_list|(
literal|false
argument_list|)
operator|.
name|setName
argument_list|(
name|endpoint
operator|.
name|getBranchName
argument_list|()
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
name|git
operator|.
name|commit
argument_list|()
operator|.
name|setAll
argument_list|(
literal|true
argument_list|)
operator|.
name|setMessage
argument_list|(
name|commitMessage
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"There was an error in Git "
operator|+
name|operation
operator|+
literal|" operation"
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getLocalRepository ()
specifier|private
name|Repository
name|getLocalRepository
parameter_list|()
block|{
name|FileRepositoryBuilder
name|builder
init|=
operator|new
name|FileRepositoryBuilder
argument_list|()
decl_stmt|;
name|Repository
name|repo
init|=
literal|null
decl_stmt|;
try|try
block|{
name|repo
operator|=
name|builder
operator|.
name|setGitDir
argument_list|(
operator|new
name|File
argument_list|(
name|endpoint
operator|.
name|getLocalPath
argument_list|()
argument_list|,
literal|".git"
argument_list|)
argument_list|)
operator|.
name|readEnvironment
argument_list|()
comment|// scan environment GIT_* variables
operator|.
name|findGitDir
argument_list|()
comment|// scan up the file system tree
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"There was an error, cannot open "
operator|+
name|endpoint
operator|.
name|getLocalPath
argument_list|()
operator|+
literal|" repository"
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|repo
return|;
block|}
block|}
end_class

end_unit

