package handler;

import service.UserService;

public class ClearGameHandler
{
    public void clear(ClearGameHandler ClearGameRequest)
    {
        UserService.cleargame(ClearGameRequest);
    }

}
